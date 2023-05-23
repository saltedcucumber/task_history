package com.cryptoexchange.mobile.presentation.trading.ordershistory

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.TradeInteractor
import com.cryptoexchange.mobile.domain.interactor.market.TradingMarketInteractor
import com.cryptoexchange.source.entity.market.MarketModel
import javax.inject.Inject

class OrdersHistoryPresenter @Inject constructor(
    private val tradeInteractor: TradeInteractor,
    private val tradingMarketInteractor: TradingMarketInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<OrdersHistoryView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        tradingMarketInteractor
            .marketObservable()
            .subscribe(
                {
                    loadHistory(it.id)
                    updateColumnHeaders(it)
                },
                { /* no-op */ }
            )
    }

    private fun updateColumnHeaders(marketModel: MarketModel) {
        val amountHeader = resourceManager.getString(
            R.string.orders_history_amount_pattern,
            marketModel.leftCurrencyCode
        )
        val priceHeader = resourceManager.getString(
            R.string.orders_history_price_pattern,
            marketModel.rightCurrencyCode
        )
        viewState.updateAmountColumn(amountHeader)
        viewState.updatePriceColumn(priceHeader)
    }

    private fun loadHistory(marketId: Long) {
        tradeInteractor
            .getRecentTrades(marketId)
            .subscribe(
                { viewState.showHistory(it) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }
}
