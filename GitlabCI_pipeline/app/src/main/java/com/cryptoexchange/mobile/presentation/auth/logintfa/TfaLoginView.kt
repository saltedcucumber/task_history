package com.cryptoexchange.mobile.presentation.auth.logintfa

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TfaLoginView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateTo(destinationId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMissingTwoFaError(message: String)
}