package com.cryptoexchange.mobile.presentation.more.transactionhistory.filter

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TransactionHistoryFilterView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showDatePicker()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun clearFields()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun applyFilter(settings: TransactionFilterSettings?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupPreviousFilterSettings(settings: TransactionFilterSettings?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupPaymentTypeSelector(paymentTypes: List<String>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupCurrencySelector(currencies: List<String>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}
