package com.cryptoexchange.mobile.presentation.trading.chart

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseBindingFragment
import com.cryptoexchange.mobile.core.base.FragmentViewBindingInflateProvider
import com.cryptoexchange.mobile.databinding.FragmentTradingChartBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.invisible
import com.cryptoexchange.mobile.extensions.visible
import com.cryptoexchange.source.entity.trade.CandleModel
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.models.CandlestickData
import com.tradingview.lightweightcharts.api.series.models.Time
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.*
import javax.inject.Inject

class ChartFragment : BaseBindingFragment<FragmentTradingChartBinding>(), ChartView {

    @Inject
    @InjectPresenter
    lateinit var presenter: ChartPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun getBinding(): FragmentViewBindingInflateProvider<FragmentTradingChartBinding> =
        FragmentTradingChartBinding::inflate

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun setupUI() {
        super.setupUI()

        val intColor = ContextCompat.getColor(requireContext(), R.color.alto).toIntColor()
        binding.tradingChart.api.applyOptions {
            layout = LayoutOptions(
                backgroundColor = Color.WHITE.toIntColor(),
                background = SolidColor(Color.WHITE.toIntColor()),
                textColor = ContextCompat.getColor(requireContext(), R.color.gray).toIntColor()
            )
            leftPriceScale = PriceScaleOptions(borderColor = intColor)
            rightPriceScale = PriceScaleOptions(borderColor = intColor)
            timeScale = TimeScaleOptions(borderColor = intColor)
            crosshair = CrosshairOptions(mode = CrosshairMode.NORMAL)
            grid = GridOptions(
                vertLines = GridLineOptions(color = intColor),
                horzLines = GridLineOptions(intColor)
            )
        }
    }

    override fun showChart(candleModels: List<CandleModel>) {
        if (candleModels.isNotEmpty()) {
            val greenColor =
                ContextCompat.getColor(requireContext(), R.color.puertoRico).toIntColor()
            val options = CandlestickSeriesOptions(
                upColor = greenColor,
                downColor = Color.WHITE.toIntColor(),
                borderUpColor = greenColor,
                borderDownColor = greenColor,
                wickUpColor = greenColor,
                wickDownColor = greenColor
            )

            val data = candleModels.map {
                val date = Date()
                date.time = it.time
                CandlestickData(
                    Time.Utc.fromDate(date),
                    it.o.toFloat(),
                    it.h.toFloat(),
                    it.l.toFloat(),
                    it.c.toFloat()
                )
            }

            binding.tradingChart.api.addCandlestickSeries(options) {
                it.setData(data)
            }

            binding.emptyChartIcon.invisible()
            binding.emptyChartText.invisible()
        } else {
            binding.emptyChartIcon.visible()
            binding.emptyChartText.visible()
        }
    }
}