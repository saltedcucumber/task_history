package com.cryptoexchange.mobile.domain.entity.socket

import com.google.gson.annotations.SerializedName

data class LoginMessage(
    @SerializedName("accountId")
    val accountId: Long,
    @SerializedName("jwt")
    val token: String
)
