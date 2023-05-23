package com.cryptoexchange.mobile.presentation.more.transactionhistory.filter

import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.domain.entity.filter.FilterDisplayValues
import com.cryptoexchange.mobile.domain.interactor.ExchangeInteractor
import com.cryptoexchange.mobile.extensions.DD_MM_YYYY
import com.cryptoexchange.mobile.extensions.fromRange
import javax.inject.Inject

class TransactionHistoryFilterPresenter @Inject constructor(
    private val exchangeInteractor: ExchangeInteractor
) :
    BasePresenter<TransactionHistoryFilterView>() {

    private var settings: TransactionFilterSettings? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        composePaymentTypesListAndSetup()
        composeCurrencyListAndSetup()
        viewState.setupPreviousFilterSettings(settings)
    }

    fun setData(settings: TransactionFilterSettings?) {
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
        currency: String
    ) {
        val settings = (dateRange.isEmpty() && type.isEmpty() && currency.isEmpty())
            .takeIf { it.not() }
            ?.run {
                val (dateFrom, dateTo) = dateRange.fromRange("-", DD_MM_YYYY)
                TransactionFilterSettings(
                    dateFrom = dateFrom,
                    dateTo = dateTo,
                    type = type,
                    currency = currency
                )
            }
        viewState.applyFilter(settings)
    }

    private fun composePaymentTypesListAndSetup() {
        val paymentTypes = FilterDisplayValues(PaymentType).displayValues
        viewState.setupPaymentTypeSelector(paymentTypes)
    }

    private fun composeCurrencyListAndSetup() {
        val currencies = FilterDisplayValues.createFromList(
            exchangeInteractor.cachedCurrencies.map { it.shortName }
        ).displayValues
        viewState.setupCurrencySelector(currencies)
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }
}
