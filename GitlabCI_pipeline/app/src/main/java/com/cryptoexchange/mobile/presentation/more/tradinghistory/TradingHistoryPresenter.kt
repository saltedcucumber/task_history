package com.cryptoexchange.mobile.presentation.more.tradinghistory

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.entity.TradingHistory
import com.cryptoexchange.mobile.domain.interactor.TradeInteractor
import com.cryptoexchange.mobile.extensions.DD_MM_YYYY_HH_MM
import com.cryptoexchange.mobile.extensions.clearAndAddAll
import com.cryptoexchange.mobile.extensions.toDateString
import com.cryptoexchange.mobile.presentation.more.tradinghistory.details.TradingHistoryDetails
import com.cryptoexchange.mobile.presentation.more.tradinghistory.details.TradingHistoryDetailsBottomSheet
import com.cryptoexchange.mobile.presentation.more.tradinghistory.filter.TradingFilterSettings
import com.cryptoexchange.mobile.presentation.more.tradinghistory.filter.TradingHistoryFilterFragment
import javax.inject.Inject

class TradingHistoryPresenter @Inject constructor(
    private val tradeInteractor: TradeInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<TradingHistoryView>() {

    private val tradings = mutableListOf<TradingHistory>()
    private var filterSettings: TradingFilterSettings? = null
    private var isLoadInProgress = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadNewPage()
    }

    fun onFilterClicked() {
        viewState.showFilter(
            R.id.action_tradeHistoryFragment_to_tradingHistoryFilterFragment,
            filterSettings?.let { TradingHistoryFilterFragment.getBundle(it) }
        )
    }

    fun applyFilter(settings: TradingFilterSettings?) {
        if (settings != filterSettings) {
            filterSettings = settings
            loadNewPage()
        }
        val icon = if (settings == null) {
            R.drawable.ic_filter
        } else {
            R.drawable.ic_filter_applied
        }
        viewState.updateFilterIcon(icon)
    }

    fun onDetailsClicked(position: Int) {
        val details = tradings.getOrNull(position)?.let {
            TradingHistoryDetails(
                orderId = it.orderId,
                type = it.type,
                leftCurrency = it.leftCurrencyCode,
                rightCurrency = it.rightCurrencyCode,
                date = it.date.toDateString(DD_MM_YYYY_HH_MM),
                side = it.side,
                price = it.price,
                fee = it.fee,
                amount = it.amount
            )
        }
        viewState.showDetails(
            R.id.action_tradeHistoryFragment_to_tradingHistoryDetailsBottomSheet,
            details?.let { TradingHistoryDetailsBottomSheet.getBundle(it) }
        )
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }

    fun onNewPageNeed() {
        loadNewPage()
    }

    private fun loadNewPage() {
        if (!isLoadInProgress) {
            tradeInteractor
                .getTradeHistory(
                    limitQuery = ELEMENTS_LIMIT,
                    offsetQuery = ELEMENTS_LIMIT,
                    dateFromQuery = filterSettings?.dateFromBackendFormatted,
                    dateToQuery = filterSettings?.dateToBackendFormatted,
                    typeQuery = filterSettings?.typeBackend,
                    sideQuery = filterSettings?.sideBackend,
                )
                .doOnSubscribe { isLoadInProgress = true }
                .doAfterTerminate { isLoadInProgress = false }
                .subscribe(
                    { onPageLoaded(it) },
                    {
                        errorHandler.handleError(it) { message ->
                            viewState.showMessage(message, MessageType.FAILED)
                        }
                    }
                )
                .connect()
        }
    }

    private fun onPageLoaded(history: List<TradingHistory>) {
        tradings.clearAndAddAll(history)
        with(viewState) {
            showTradings(history)
        }
    }

    companion object {
        private const val ELEMENTS_LIMIT = 0
    }
}
