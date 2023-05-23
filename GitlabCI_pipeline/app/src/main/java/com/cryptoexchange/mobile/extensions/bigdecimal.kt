package com.cryptoexchange.mobile.extensions

import java.math.BigDecimal
import java.math.MathContext

private const val DISPLAY_LENGTH = 8
private const val BIG_DECIMAL_100 = 100

fun BigDecimal?.smaller(obj: BigDecimal?): Boolean =
    this?.compareTo(obj)?.equals(-1) ?: false

fun BigDecimal?.identical(obj: BigDecimal?): Boolean =
    this?.compareTo(obj)?.equals(0) ?: false

fun BigDecimal?.bigger(obj: BigDecimal?): Boolean =
    this?.compareTo(obj)?.equals(1) ?: false

fun BigDecimal?.notIdentical(obj: BigDecimal?): Boolean = !this.identical(obj)

fun BigDecimal?.biggerOrIdentical(obj: BigDecimal?): Boolean =
    this.bigger(obj) || this.identical(obj)

fun BigDecimal?.smallerOrIdentical(obj: BigDecimal?): Boolean =
    this.smaller(obj) || this.identical(obj)

fun BigDecimal.compareWith(obj: BigDecimal): CompareRes? = CompareRes.fromInt(this.compareTo(obj))

fun BigDecimal.toPlainStringExceptZero(): String = if (identical(BigDecimal.ZERO)) {
    ""
} else {
    stripTrailingZeros().toPlainString()
}

fun BigDecimal.multiplyWithPrecision(
    multiplier: BigDecimal,
    mathContext: MathContext = MathContext.DECIMAL128
): BigDecimal {
    return multiply(multiplier, mathContext)
}

fun BigDecimal.toDisplayStringBy(): String {
    val amount = this.stripTrailingZeros()
    return amount.toPlainString()
}

fun BigDecimal.calculateCommission(fee: BigDecimal): BigDecimal {
    return this.divide(BigDecimal(BIG_DECIMAL_100)).multiply(fee)
}

enum class CompareRes(val value: Int) {
    BIGGER(1),
    IDENTICAL(0),
    SMALLER(-1);

    companion object {
        fun fromInt(value: Int) = CompareRes.values().firstOrNull { it.value == value }
    }
}
