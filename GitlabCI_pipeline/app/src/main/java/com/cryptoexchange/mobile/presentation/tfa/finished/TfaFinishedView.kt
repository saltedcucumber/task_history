package com.cryptoexchange.mobile.presentation.tfa.finished

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TfaFinishedView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun endTfaFlow()
}
