package com.cryptoexchange.mobile.presentation.auth.resetpasssuccess

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ResetPassSuccessView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showSuccessMessage(email: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openEmail()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}