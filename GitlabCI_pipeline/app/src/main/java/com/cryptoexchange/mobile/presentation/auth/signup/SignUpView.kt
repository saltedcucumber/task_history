package com.cryptoexchange.mobile.presentation.auth.signup

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SignUpView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showFirstNameError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showLastNameError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showEmailError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showPasswordError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showConfirmPasswordError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateTo(destinationId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}
