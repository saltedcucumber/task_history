package com.cryptoexchange.mobile.presentation.trading.ordershistory

import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.source.entity.trade.history.recent.TradeRecentOrderHistoryModel
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface OrdersHistoryView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showHistory(data: List<TradeRecentOrderHistoryModel>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateAmountColumn(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updatePriceColumn(text: String)
}
