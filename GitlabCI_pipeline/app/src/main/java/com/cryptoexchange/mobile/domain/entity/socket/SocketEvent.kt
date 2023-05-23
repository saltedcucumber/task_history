package com.cryptoexchange.mobile.domain.entity.socket

import com.google.gson.annotations.SerializedName

data class SocketEvent<T>(
    @SerializedName("eventType")
    val eventType: EventType,
    @SerializedName("data")
    val data: T?
)
