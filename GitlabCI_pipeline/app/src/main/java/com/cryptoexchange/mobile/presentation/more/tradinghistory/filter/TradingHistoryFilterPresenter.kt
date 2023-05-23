package com.cryptoexchange.mobile.presentation.more.tradinghistory.filter

import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.domain.entity.filter.FilterDisplayValues
import com.cryptoexchange.mobile.extensions.DD_MM_YYYY
import com.cryptoexchange.mobile.extensions.fromRange
import com.cryptoexchange.mobile.presentation.exchange.ExchangeType
import com.cryptoexchange.mobile.presentation.exchange.TransactionType
import javax.inject.Inject

class TradingHistoryFilterPresenter @Inject constructor() :
    BasePresenter<TradingHistoryFilterView>() {

    private var settings: TradingFilterSettings? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        composeTransactionTypesListAndSetup()
        composeExchangeTypesListAndSetup()
        viewState.setupPreviousFilterSettings(settings)
    }

    fun setData(settings: TradingFilterSettings?) {
        this.settings = settings
    }

    fun onDatePickerClicked() {
        viewState.showDatePicker()
    }

    fun onResetClicked() {
        viewState.clearFields()
    }

    fun onApplyFilterClicked(
        dateRange: String,
        type: String,
        side: String
    ) {
        val settings = (dateRange.isEmpty() && type.isEmpty() && side.isEmpty())
            .takeIf { it.not() }
            ?.run {
                val (dateFrom, dateTo) = dateRange.fromRange("-", DD_MM_YYYY)
                TradingFilterSettings(
                    dateFrom = dateFrom,
                    dateTo = dateTo,
                    type = type,
                    side = side
                )
            }
        viewState.applyFilter(settings)
    }

    private fun composeTransactionTypesListAndSetup() {
        val transactionTypes = FilterDisplayValues(TransactionType).displayValues
        viewState.setupTransactionTypeSelector(transactionTypes)
    }

    private fun composeExchangeTypesListAndSetup() {
        val exchangesTypes = FilterDisplayValues(ExchangeType).displayValues
        viewState.setupExchangeTypeSelector(exchangesTypes)
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }
}
