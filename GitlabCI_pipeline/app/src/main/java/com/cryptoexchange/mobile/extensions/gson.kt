package com.cryptoexchange.mobile.extensions

import com.cryptoexchange.mobile.domain.entity.socket.EventType
import com.cryptoexchange.mobile.domain.entity.socket.SocketEvent
import com.cryptoexchange.mobile.domain.entity.socket.Tickers
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken

fun Gson.fromJsonToSocketEvent(jsonStr: String): SocketEvent<*> {
    val jsonObject = JsonParser().parse(jsonStr).asJsonObject
    val eventType =
        EventType.values().find { it.toString() == jsonObject.get("eventType").asString }

    return when (eventType) {
        else -> {
            val type = TypeToken.getParameterized(SocketEvent::class.java, Tickers::class.java).type
            fromJson<SocketEvent<Tickers>>(jsonStr, type)
        }
    }
}
