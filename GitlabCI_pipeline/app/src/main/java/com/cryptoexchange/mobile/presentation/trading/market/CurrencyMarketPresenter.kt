package com.cryptoexchange.mobile.presentation.trading.market

import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.TradeInteractor
import com.cryptoexchange.mobile.domain.interactor.market.TradingMarketInteractor
import com.cryptoexchange.source.entity.market.MarketModel
import javax.inject.Inject

class CurrencyMarketPresenter @Inject constructor(
    private val tradingMarketInteractor: TradingMarketInteractor,
    private val tradeInteractor: TradeInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<CurrencyMarketView>() {

    private var market: MarketModel? = null
    private lateinit var currentMarketModel: MarketModel

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadMarkets()
    }

    fun data(marketModel: MarketModel) {
        currentMarketModel = marketModel
    }

    private fun loadMarkets() {
        tradeInteractor
            .getMarkets()
            .subscribe(
                { marketModels ->
                    val position = marketModels.indexOfFirst { it.id == currentMarketModel.id }
                    viewState.setupCurrencyAdapter(marketModels, position)
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
    }

    fun onApplyClicked() {
        market?.let { tradingMarketInteractor.push(it) }
        viewState.navigateBack()
    }

    fun onCurrencyPairChanged(model: MarketModel) {
        market = model
    }
}
