package com.cryptoexchange.mobile.domain.entity.backendwrappers

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.global.ResourceManager
import java.math.BigDecimal
import java.math.RoundingMode

typealias ConvertedCurrencyShortName = String
typealias Amount = BigDecimal
typealias ConvertedAmount = BigDecimal

class CurrencyWrapper(
    val id: Long,
    val currencyShortName: String,
    val converted: Triple<Amount, ConvertedAmount, ConvertedCurrencyShortName>
) {

    fun buildTokensRemainsString(resourceManager: ResourceManager): String {
        val (amount, convertedAmount, _) = converted
        val pattern = resourceManager.getString(R.string.remains_pattern)
        return String.format(
            pattern,
            amount.setScale(2, RoundingMode.DOWN),
            currencyShortName,
            "$",
            convertedAmount.setScale(2, RoundingMode.DOWN)
        )
    }
}
