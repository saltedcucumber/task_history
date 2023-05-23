package com.cryptoexchange.mobile.presentation.trading.myorders

import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.source.entity.order.OrderModel
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MyOrdersView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showOrders(orders: List<Pair<OrderModel, String>>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showConfirmationDialog(position: Int)
}
