package com.cryptoexchange.mobile.presentation.tfa.disable

import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface DisableTfaView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToDisableSuccess(@IdRes actionId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorMessages(passwordMessage: String, codeMessage: String)
}
