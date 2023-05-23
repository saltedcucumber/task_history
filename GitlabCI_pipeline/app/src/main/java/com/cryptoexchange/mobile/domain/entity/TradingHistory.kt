package com.cryptoexchange.mobile.domain.entity

import com.cryptoexchange.source.entity.order.OrderSide
import com.cryptoexchange.source.entity.order.OrderType
import java.math.BigDecimal

data class TradingHistory(
    val tradeId: Long,
    val leftCurrencyCode: String,
    val rightCurrencyCode: String,
    val amount: BigDecimal,
    val price: BigDecimal,
    val date: Long,
    val side: OrderSide,
    val orderId: Long,
    val type: OrderType,
    val fee: BigDecimal,
) {

    val received: BigDecimal = amount.multiply(price)
}
