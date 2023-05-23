package com.cryptoexchange.mobile.presentation.balancecontainer.portfolio.adapter

import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.databinding.ItemPortfolioBinding
import com.cryptoexchange.mobile.domain.entity.CurrencyInBasic
import com.cryptoexchange.source.extensions.getCurrencyIcon
import java.math.BigDecimal
import java.math.RoundingMode

class PortfolioViewHolder(
    private val binding: ItemPortfolioBinding,
    private val totalBalance: BigDecimal
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(currency: CurrencyInBasic) {
        with(binding) {
            currencyCode.text = currency.currencyCode

            val availableFormatted = currency
                .available
                .setScale(AVAILABLE_CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
                .toPlainString()
            currencyInUsd.text = String.format(BASE_CURRENCY_PATTERN, availableFormatted)

            currencyIcon.setImageResource(getCurrencyIcon(currency.currencyCode))

            val hundredPercents = BigDecimal.valueOf(HUNDRED_PERCENTS)
            val onePercent = totalBalance.divide(hundredPercents, RoundingMode.HALF_EVEN)
            val percents = currency.available.divide(onePercent, RoundingMode.HALF_EVEN)
            val percentsString = percents
                .setScale(AVAILABLE_CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
                .stripTrailingZeros()
                .toPlainString()
            currencyPercentage.text = String.format(PERCENTS_PATTERN, percentsString)
        }
    }

    companion object {
        private const val AVAILABLE_CURRENCY_LENGTH = 2
        private const val HUNDRED_PERCENTS = 100L
        private const val PERCENTS_PATTERN = "/%s%%"
        private const val BASE_CURRENCY_PATTERN = "$ %s"
    }
}