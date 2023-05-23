package com.cryptoexchange.mobile.presentation.trading.chart

import android.text.format.DateUtils
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.TradeInteractor
import com.cryptoexchange.mobile.domain.interactor.market.TradingMarketInteractor
import com.cryptoexchange.mobile.extensions.THIRTY_DAYS
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class ChartPresenter @Inject constructor(
    private val tradingMarketInteractor: TradingMarketInteractor,
    private val errorHandler: ErrorHandler,
    private val tradeInteractor: TradeInteractor
) : BasePresenter<ChartView>() {

    private var tradingMarketDisposable: Disposable? = null

    override fun attachView(view: ChartView?) {
        super.attachView(view)

        observeMarketIdChanges()
    }

    override fun detachView(view: ChartView?) {
        super.detachView(view)

        tradingMarketDisposable?.dispose()
        tradingMarketDisposable = null
    }

    private fun observeMarketIdChanges() {
        tradingMarketDisposable = tradingMarketInteractor
            .marketObservable()
            .subscribe(
                { loadMarketHistory(it.id) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
    }

    private fun loadMarketHistory(marketId: Long) {
        val todayMills = System.currentTimeMillis()
        val monthAgoMills = todayMills - DateUtils.DAY_IN_MILLIS * THIRTY_DAYS

        tradeInteractor
            .getOhlcvHistory(marketId, monthAgoMills, todayMills)
            .subscribe(
                { viewState.showChart(it.candleList) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }
}