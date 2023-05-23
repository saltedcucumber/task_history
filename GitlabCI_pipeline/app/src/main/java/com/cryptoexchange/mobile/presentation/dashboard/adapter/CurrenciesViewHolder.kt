package com.cryptoexchange.mobile.presentation.dashboard.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.databinding.ItemDashboardCurrenciesBinding
import com.cryptoexchange.mobile.domain.entity.CurrencyHistory
import com.cryptoexchange.mobile.extensions.*
import com.cryptoexchange.source.entity.currency.converter.history.ExchangeRate
import com.cryptoexchange.source.extensions.getCurrencyIcon
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.HistogramData
import com.tradingview.lightweightcharts.api.series.models.Time
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class CurrenciesViewHolder(
    private val binding: ItemDashboardCurrenciesBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat(YYYY_MM_DD, Locale.ENGLISH)
    private var histogramSeries: SeriesApi? = null

    // Colors and drawables
    // Positive
    val mainPositive = ContextCompat.getColor(itemView.context, R.color.puertoRico)
    val percentsBackgroundPositive = ContextCompat.getColor(itemView.context, R.color.frostedMint)
    val percentsPositiveDrawable =
        ContextCompat.getDrawable(itemView.context, R.drawable.ic_arrow_up)

    // Neutral
    val mainNetral = ContextCompat.getColor(itemView.context, R.color.colorPrimary)
    val percentsBackgroundNetral = ContextCompat.getColor(itemView.context, R.color.tropicalBlue)

    // Negative
    val mainNegative = ContextCompat.getColor(itemView.context, R.color.flamingo)
    val percentsBackgroundNegative = ContextCompat.getColor(itemView.context, R.color.fairPink)
    val percentsNegativeDrawable =
        ContextCompat.getDrawable(itemView.context, R.drawable.ic_arrow_down)

    init {
        initChart()
    }

    fun bind(history: CurrencyHistory) {
        with(binding) {
            currencyIcon.setImageResource(getCurrencyIcon(history.currencyCode))
            currencyCode.text = history.currencyCode

            val bigRate = history.currencyRates.last().rate
            val rate = bigRate
                .setScale(CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
                .toPlainString()

            currencyRate.text = String.format(CURRENCY_RATE_PATTERN, rate)

            val compareRes = calculateRatePercents(history) ?: CompareRes.IDENTICAL

            updatePercentsColor(compareRes)
            showRates(history.currencyRates, compareRes)
        }
    }

    private fun getAreaSeriesOptions(compareRes: CompareRes): AreaSeriesOptions {
        val lineColor = when (compareRes) {
            CompareRes.BIGGER -> mainPositive
            CompareRes.IDENTICAL -> mainNetral
            CompareRes.SMALLER -> mainNegative
        }

        return AreaSeriesOptions(
            priceLineVisible = false,
            baseLineVisible = false,
            lineWidth = LineWidth.TWO,
            crosshairMarkerVisible = false,
            lineColor = lineColor.toIntColor(),
            topColor = lineColor.toIntColor(),
            bottomColor = Color.WHITE.toIntColor()
        )
    }

    private fun showRates(currencyRates: List<ExchangeRate>, compareRes: CompareRes) {
        val histogram = currencyRates.map {
            calendar.time = dateFormat.parseOrNull(it.date) ?: Date()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            HistogramData(Time.BusinessDay(year, month, day), it.rate.toFloat())
        }

        histogramSeries?.let {
            binding.currencyChart.api.removeSeries(it) {}
        }
        binding.currencyChart.api.addAreaSeries(
            getAreaSeriesOptions(compareRes)
        ) {
            histogramSeries = it
            it.setData(histogram)
        }
    }

    private fun calculateRatePercents(history: CurrencyHistory): CompareRes? {
        val lastIndex = history.currencyRates.lastIndex
        val lastRate = history.currencyRates[lastIndex].rate
        val prevRate = history.currencyRates[lastIndex - 1].rate

        val changeStatus = lastRate.compareWith(prevRate)
        val scale = lastRate.toPlainString().length
        val bigPercents = lastRate
            .divide(prevRate, scale, RoundingMode.HALF_EVEN)
            .multiply(hundred)
            .minus(hundred)
        val percents = bigPercents
            .abs()
            .setScale(CURRENCY_LENGTH, RoundingMode.FLOOR)
            .stripTrailingZeros()
            .toPlainString()

        binding.currencyPercentage.text = String.format(PERCENTS_PATTERN, percents)

        return changeStatus
    }

    private fun updatePercentsColor(compareRes: CompareRes) {
        val textColor: Int
        val backgroundColor: Int
        val textDrawable: Drawable?
        when (compareRes) {
            CompareRes.BIGGER -> {
                textColor = mainPositive
                backgroundColor = percentsBackgroundPositive
                textDrawable = percentsPositiveDrawable
            }
            CompareRes.IDENTICAL -> {
                textColor = mainNetral
                backgroundColor = percentsBackgroundNetral
                textDrawable = null
            }
            CompareRes.SMALLER -> {
                textColor = mainNegative
                backgroundColor = percentsBackgroundNegative
                textDrawable = percentsNegativeDrawable
            }
        }

        with(binding.currencyPercentage) {
            background = getStatusDrawable(backgroundColor, background)
            setTextColor(textColor)
            setCompoundDrawablesWithIntrinsicBounds(null, null, textDrawable, null)
        }
    }

    private fun getStatusDrawable(color: Int, drawable: Drawable): Drawable {
        when (drawable) {
            is ShapeDrawable -> {
                drawable.paint.color = color
            }
            is GradientDrawable -> {
                drawable.setColor(color)
            }
            is ColorDrawable -> {
                drawable.color = color
            }
        }

        return drawable
    }

    private fun initChart() {
        binding.currencyChart.api.applyOptions {
            layout = layoutOptions {
                backgroundColor = Color.WHITE.toIntColor()
            }
            leftPriceScale = PriceScaleOptions(visible = false)
            rightPriceScale = PriceScaleOptions(visible = false)
            timeScale = TimeScaleOptions(visible = false)
            crosshair = CrosshairOptions(
                vertLine = crosshairLineOptions { visible = false },
                horzLine = crosshairLineOptions { visible = false }
            )
            grid = GridOptions(
                vertLines = GridLineOptions(visible = false),
                horzLines = GridLineOptions(visible = false)
            )
        }
    }

    companion object {
        private const val CURRENCY_LENGTH = 2
        private const val CURRENCY_RATE_PATTERN = "%s $"
        private const val PERCENTS_PATTERN = "%s %%"

        private val hundred = BigDecimal("100")
    }
}