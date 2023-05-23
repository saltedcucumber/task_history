package com.cryptoexchange.mobile.presentation.exchange.filters

import com.cryptoexchange.source.entity.currency.CurrencyResponse
import com.cryptoexchange.source.entity.currency.converter.price.CurrencyPriceModel
import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.mobile.presentation.exchange.filters.data.CurrencyDropDown
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface CurrencyChooserView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupCurrencyAdapter(
        displayCurrencies: List<CurrencyResponse>,
        from: CurrencyDropDown
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun moveBackward(
        currencyResponse: CurrencyResponse,
        from: CurrencyDropDown,
        prices: List<CurrencyPriceModel>
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}
