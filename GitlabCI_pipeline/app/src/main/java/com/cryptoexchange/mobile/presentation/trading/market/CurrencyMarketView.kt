package com.cryptoexchange.mobile.presentation.trading.market

import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.source.entity.market.MarketModel
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface CurrencyMarketView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupCurrencyAdapter(markets: List<MarketModel>, currencyPair: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}
