package com.cryptoexchange.mobile.presentation.more

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MoreView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun restart()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun inDevelopment()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showUserVerificationStatus(statusIconId: Int, messageId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateTo(destinationId: Int)
}
