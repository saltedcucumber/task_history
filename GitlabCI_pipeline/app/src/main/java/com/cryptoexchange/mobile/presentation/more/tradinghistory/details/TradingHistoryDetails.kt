package com.cryptoexchange.mobile.presentation.more.tradinghistory.details

import android.content.Context
import android.os.Parcelable
import com.cryptoexchange.mobile.extensions.CURRENCY_LENGTH
import com.cryptoexchange.mobile.extensions.toDisplayStringBy
import com.cryptoexchange.source.entity.order.OrderSide
import com.cryptoexchange.source.entity.order.OrderType
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.MathContext
import java.math.RoundingMode

@Parcelize
data class TradingHistoryDetails(
    val orderId: Long,
    val type: OrderType,
    val leftCurrency: String,
    val rightCurrency: String,
    val date: String,
    val side: OrderSide,
    val price: BigDecimal,
    val fee: BigDecimal,
    val amount: BigDecimal
) : Parcelable {

    private val currencyPair
        get() = kotlin.run {
            when (side) {
                OrderSide.BUY -> rightCurrency to leftCurrency
                OrderSide.SELL -> leftCurrency to rightCurrency
                else -> rightCurrency to leftCurrency
            }
        }

    private val displayPriceValue
        get() = kotlin.run {
            when (side) {
                OrderSide.BUY -> price
                OrderSide.SELL -> ONE.divide(price, MathContext.DECIMAL128)
                    .setScale(CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
                else -> price
            }
        }

    val displayPrice: String
        get() = "$ONE ${currencyPair.second} - " +
            "${displayPriceValue.toDisplayStringBy()} ${currencyPair.first}"

    val displayFee: String
        get() = "${fee.toDisplayStringBy()} ${currencyPair.second}"

    val displayTotal: String
        get() = kotlin.run {
            val total = side.getTotal(amount, fee, price)?.toDisplayStringBy()
            "$total ${currencyPair.second}"
        }

    fun getDisplaySide(context: Context) = side.displayValue(context)

    fun getDisplayType(context: Context) = type.displayValue(context)
}
