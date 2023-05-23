package com.cryptoexchange.mobile.presentation.trading

import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.TradeInteractor
import com.cryptoexchange.mobile.domain.interactor.market.TradingMarketInteractor
import com.cryptoexchange.mobile.presentation.trading.market.CurrencyMarketFragment
import com.cryptoexchange.source.entity.market.MarketModel
import javax.inject.Inject

class TradingPresenter @Inject constructor(
    private val tradingMarketInteractor: TradingMarketInteractor,
    private val tradeInteractor: TradeInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<TradingView>() {

    private var marketModel: MarketModel? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        observeTradingMarket()
        loadMarkets()
    }

    fun onChangePairClicked() {
        marketModel?.let {
            val bundle = CurrencyMarketFragment.getBundle(it)
            viewState.navigateToCurrencyMarketScreen(bundle)
        }
    }

    fun onTabChanged(position: Int) {
        viewState.showCurrencyPairButton(position != MY_ORDERS_ID)
    }

    private fun loadMarkets() {
        tradeInteractor
            .getMarkets()
            .subscribe(
                { markets ->
                    marketModel = markets.firstOrNull()
                    val market = marketModel?.let {
                        tradingMarketInteractor.push(it)
                        "${it.leftCurrencyCode} - ${it.rightCurrencyCode}"
                    } ?: ""
                    viewState.updateCurrencyPair(market)
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
    }

    private fun observeTradingMarket() {
        tradingMarketInteractor
            .marketObservable()
            .subscribe(
                {
                    marketModel = it
                    val pair = "${it.leftCurrencyCode} - ${it.rightCurrencyCode}"
                    viewState.updateCurrencyPair(pair)
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
    }

    companion object {
        private const val MY_ORDERS_ID = 3
    }
}
