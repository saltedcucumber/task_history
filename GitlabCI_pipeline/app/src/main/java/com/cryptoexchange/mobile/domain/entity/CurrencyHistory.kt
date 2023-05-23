package com.cryptoexchange.mobile.domain.entity

import com.cryptoexchange.source.entity.currency.converter.history.ExchangeRate

data class CurrencyHistory(
    val currencyCode: String,
    val currencyRates: List<ExchangeRate>
)
