package com.cryptoexchange.mobile.presentation.trading.orders

import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.TradeInteractor
import com.cryptoexchange.mobile.domain.interactor.market.TradingMarketInteractor
import com.cryptoexchange.mobile.extensions.toDisplayStringBy
import com.cryptoexchange.source.entity.market.MarketModel
import com.cryptoexchange.source.entity.trade.orders.MarketOrderbookModel
import com.cryptoexchange.source.entity.trade.orders.OrderQueries
import javax.inject.Inject

class OrdersPresenter @Inject constructor(
    private val tradeInteractor: TradeInteractor,
    private val tradingMarketInteractor: TradingMarketInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<OrdersView>() {

    private var isOnPause = true

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observeTradingMarket()
    }

    fun onPause(isOnPause: Boolean) {
        this.isOnPause = isOnPause
    }

    private fun observeTradingMarket() {
        tradingMarketInteractor
            .marketObservable()
            .subscribe(
                { loadOrders(it) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
    }

    private fun loadOrders(marketModel: MarketModel) {
        tradeInteractor
            .getOrderbookList(
                OrderQueries(marketId = marketModel.id)
            )
            .subscribe(
                {
                    viewState.showOrders(it)
                    updateColumnHeaders(it, marketModel)
                },
                {
                    viewState.clearOrders()
                    viewState.updateBuyVolume("")
                    viewState.updateSellVolume("")
                    if (!isOnPause) {
                        errorHandler.handleError(it) { message ->
                            viewState.showMessage(message, MessageType.FAILED)
                        }
                    }
                }
            )
            .connect()
    }

    private fun updateColumnHeaders(model: MarketOrderbookModel, marketModel: MarketModel) {
        val buyVolumeSum = model.buyOrderbookList.sumOf { it.volume }.toDisplayStringBy()
        val orderVolumeSum = model.sellOrderbookList.sumOf { it.volume }.toDisplayStringBy()

        val buyVolumeText = "$buyVolumeSum ${marketModel.rightCurrencyCode}"
        val sellVolumeText = "$orderVolumeSum ${marketModel.leftCurrencyCode}"

        viewState.updateBuyVolume(buyVolumeText)
        viewState.updateSellVolume(sellVolumeText)
    }
}
