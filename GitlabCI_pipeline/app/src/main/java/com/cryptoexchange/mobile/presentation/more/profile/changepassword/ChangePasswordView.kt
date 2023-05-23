package com.cryptoexchange.mobile.presentation.more.profile.changepassword

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ChangePasswordView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTfaInputView()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showOldPasswordError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showNewPasswordError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showConfirmNewPasswordError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showTfaCodeError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToSuccess()
}