package com.cryptoexchange.mobile.presentation.exchange

import androidx.annotation.StringRes
import com.cryptoexchange.source.entity.order.OrderType
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.enumextension.NamesTransporter

enum class TransactionType(
    val tabPosition: Int,
    @StringRes val label: Int
) : ConvertibleToBackend<OrderType> {
    MARKET(0, R.string.market_tab) {
        override val convertedBackendValue: OrderType
            get() = OrderType.MARKET
    },
    LIMIT(1, R.string.limit_tab) {
        override val convertedBackendValue: OrderType
            get() = OrderType.LIMIT
    };

    companion object : NamesTransporter {

        override val names: Array<String>
            get() = ArrayList<String>(values().size).also {
                values().forEach { trType ->
                    it.add(trType.name)
                }
            }.toTypedArray()
    }
}
