package com.cryptoexchange.mobile.domain.entity.socket

import com.google.gson.annotations.SerializedName

data class SocketMessage<T>(
    @SerializedName("messageType")
    val messageType: MessageType,
    @SerializedName("loginMessage")
    val message: T?
)
