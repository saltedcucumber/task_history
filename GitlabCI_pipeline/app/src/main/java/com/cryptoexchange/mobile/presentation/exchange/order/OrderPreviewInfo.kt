package com.cryptoexchange.mobile.presentation.exchange.order

import android.os.Parcelable
import com.cryptoexchange.mobile.extensions.calculateCommission
import com.cryptoexchange.mobile.extensions.toPlainStringExceptZero
import com.cryptoexchange.mobile.presentation.exchange.TransactionType
import com.cryptoexchange.source.entity.order.OrderSide
import com.cryptoexchange.source.entity.order.OrderType
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.math.RoundingMode

@Parcelize
data class OrderPreviewInfo(
    val amount: BigDecimal,
    val paymentType: String, // ETH
    val marketId: Long,
    val total: BigDecimal, // 0,075
    val consumerType: String, // BTC
    val fee: BigDecimal,
    val orderSide: OrderSide,
    val orderType: OrderType,
    val price: BigDecimal?,
    val course: BigDecimal,
    val transactionType: TransactionType
) : Parcelable {

    val representationAmount: String
        get() = "${
        amount
            .setScale(DISPLAY_SCALE, RoundingMode.HALF_UP)
            .toPlainStringExceptZero()
            .take(DISPLAY_LENGTH)
        } $paymentType" // 1 ETH

    val representationCourse: String
        get() = kotlin.run {
            val givenCourse = when (transactionType) {
                TransactionType.LIMIT -> price ?: course
                else -> course
            }
            String.format(
                CURRENCY_PATTERN,
                paymentType,
                givenCourse
                    ?.setScale(DISPLAY_SCALE, RoundingMode.HALF_UP)
                    ?.toPlainStringExceptZero()
                    ?.take(DISPLAY_LENGTH),
                consumerType
            ) // 1 ETH = 0,075 BTC
        }

    private val displayTotal
        get() = kotlin.run {
            when (orderSide) {
                OrderSide.BUY -> total.plus(total.calculateCommission(fee))
                    .setScale(DISPLAY_SCALE, RoundingMode.HALF_UP)
                else -> total.minus(total.calculateCommission(fee))
                    .setScale(DISPLAY_SCALE, RoundingMode.HALF_UP)
            }
        }

    val representationTotal: String
        get() = "${
        displayTotal.toPlainStringExceptZero().take(DISPLAY_LENGTH)
        } $consumerType" // 0,075 BTC

    val representationFee: String
        get() = "$fee%" // 0,5%

    companion object {

        const val DISPLAY_SCALE = 10
        const val DISPLAY_LENGTH = 10

        private const val CURRENCY_PATTERN = "1 %s = %s %s"
    }
}
