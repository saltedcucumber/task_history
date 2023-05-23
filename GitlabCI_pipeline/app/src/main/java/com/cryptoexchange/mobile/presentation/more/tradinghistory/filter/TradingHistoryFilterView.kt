package com.cryptoexchange.mobile.presentation.more.tradinghistory.filter

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TradingHistoryFilterView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showDatePicker()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun clearFields()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun applyFilter(settings: TradingFilterSettings?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupPreviousFilterSettings(settings: TradingFilterSettings?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupTransactionTypeSelector(transactionTypes: List<String>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupExchangeTypeSelector(exchangesTypes: List<String>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}