package com.cryptoexchange.mobile.presentation.exchange

import com.cryptoexchange.source.entity.order.OrderSide
import com.cryptoexchange.source.entity.trade.FeeSetAccountValueResponse
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.enumextension.NamesTransporter
import com.cryptoexchange.mobile.core.global.ResourceManager
import java.math.BigDecimal

enum class ExchangeType : ConvertibleToBackend<OrderSide> {
    BUY {
        override val nextValue: ExchangeType
            get() = SELL

        override fun tradeFee(fee: FeeSetAccountValueResponse?): BigDecimal {
            return fee?.takerFee ?: BigDecimal.ZERO
        }

        override fun actionName(resourceManager: ResourceManager): String {
            return resourceManager.getString(R.string.buy_button_text)
        }

        override val convertedBackendValue: OrderSide
            get() = OrderSide.BUY
    },

    SELL {
        override val nextValue: ExchangeType
            get() = BUY

        override fun tradeFee(fee: FeeSetAccountValueResponse?): BigDecimal {
            return fee?.makerFee ?: BigDecimal.ZERO
        }

        override fun actionName(resourceManager: ResourceManager): String {
            return resourceManager.getString(R.string.sell_button_text)
        }

        override val convertedBackendValue: OrderSide
            get() = OrderSide.SELL
    };

    abstract val nextValue: ExchangeType

    abstract fun tradeFee(fee: FeeSetAccountValueResponse?): BigDecimal

    abstract fun actionName(resourceManager: ResourceManager): String

    companion object : NamesTransporter {

        override val names: Array<String>
            get() = ArrayList<String>(values().size).also {
                values().forEach { exType ->
                    it.add(exType.name)
                }
            }.toTypedArray()
    }
}
