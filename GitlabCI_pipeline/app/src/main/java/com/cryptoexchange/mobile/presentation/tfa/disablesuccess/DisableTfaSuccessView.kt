package com.cryptoexchange.mobile.presentation.tfa.disablesuccess

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface DisableTfaSuccessView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openEmail()
}