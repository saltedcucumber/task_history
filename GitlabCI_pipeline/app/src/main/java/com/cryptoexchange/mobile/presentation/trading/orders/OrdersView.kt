package com.cryptoexchange.mobile.presentation.trading.orders

import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.source.entity.trade.orders.MarketOrderbookModel
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface OrdersView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateBuyVolume(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateSellVolume(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class, tag = TAG_ORDERS)
    fun showOrders(data: MarketOrderbookModel)

    @StateStrategyType(AddToEndSingleStrategy::class, tag = TAG_ORDERS)
    fun clearOrders()

    companion object {
        private const val TAG_ORDERS = "TAG_ORDERS"
    }
}
