package com.cryptoexchange.mobile.domain.entity.socket

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

sealed class EventData

data class Tickers(
    @SerializedName("marketId")
    val marketId: Long,
    @SerializedName("price")
    val price: BigDecimal
) : EventData()
