package com.cryptoexchange.mobile.presentation.welcome

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface WelcomeView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateTo(destinationId: Int)
}
