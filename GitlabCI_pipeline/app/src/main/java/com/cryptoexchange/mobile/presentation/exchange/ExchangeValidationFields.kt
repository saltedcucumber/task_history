package com.cryptoexchange.mobile.presentation.exchange

import java.math.BigDecimal
import java.math.BigDecimal.ZERO

data class ExchangeValidationFields(
    val given: BigDecimal,
    val received: BigDecimal,
    val price: BigDecimal
) {

    fun isValid(transactionType: TransactionType): Boolean {
        return when (transactionType) {
            TransactionType.LIMIT -> given != ZERO && received != ZERO && price != ZERO
            TransactionType.MARKET -> given != ZERO && received != ZERO
        }
    }
}
