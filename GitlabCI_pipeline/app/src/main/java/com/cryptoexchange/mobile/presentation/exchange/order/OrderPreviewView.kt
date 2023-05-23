package com.cryptoexchange.mobile.presentation.exchange.order

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface OrderPreviewView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToSuccessfulConversionScreen()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}
