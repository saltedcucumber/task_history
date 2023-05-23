package com.cryptoexchange.mobile.domain.entity

import java.math.BigDecimal

data class CurrencyInBasic(
    val currencyCode: String,
    val available: BigDecimal
)
