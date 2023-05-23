package com.cryptoexchange.mobile.presentation.exchange.filters.data

import android.os.Parcelable
import com.cryptoexchange.source.entity.currency.CurrencyResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrencyChoosingInfo(
    val what: List<CurrencyResponse>,
    val from: CurrencyDropDown
) : Parcelable
