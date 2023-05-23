package com.cryptoexchange.mobile.presentation.trading.myorders

import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.ExchangeInteractor
import com.cryptoexchange.source.entity.order.OrderModel
import io.reactivex.rxjava3.core.Observable.combineLatest
import javax.inject.Inject

class MyOrdersPresenter @Inject constructor(
    private val exchangeInteractor: ExchangeInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<MyOrdersView>() {

    private val orders = mutableListOf<Pair<OrderModel, String>>()

    override fun attachView(view: MyOrdersView?) {
        super.attachView(view)

        loadOrders()
    }

    fun onCloseClicked(position: Int) {
        viewState.showConfirmationDialog(position)
    }

    fun onCloseOrderSuggested(position: Int) {
        exchangeInteractor
            .closeOrder(orders[position].first.id ?: -1)
            .subscribe(
                { loadOrders() },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }

    private fun loadOrders() {
        combineLatest(
            exchangeInteractor.getOrdersList().toObservable(),
            exchangeInteractor.marketList().toObservable()
        ) { wrapper, markets ->
            val orders = wrapper.orders

            orders.map { order ->
                val market = markets.find { it.id == order.marketId }
                val currencyPair = if (market != null) {
                    "${market.leftCurrencyCode}-${market.rightCurrencyCode}"
                } else {
                    ""
                }
                Pair(order, currencyPair)
            }
        }
            .subscribe(
                {
                    orders.clear()
                    orders.addAll(it)
                    viewState.showOrders(orders)
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }
}
