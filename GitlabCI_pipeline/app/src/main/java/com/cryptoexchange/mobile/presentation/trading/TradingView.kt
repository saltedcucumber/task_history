package com.cryptoexchange.mobile.presentation.trading

import android.os.Bundle
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TradingView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateCurrencyPair(text: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToCurrencyMarketScreen(bundle: Bundle)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCurrencyPairButton(isShow: Boolean)
}
