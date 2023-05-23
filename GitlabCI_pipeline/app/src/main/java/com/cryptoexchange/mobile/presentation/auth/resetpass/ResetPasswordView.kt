package com.cryptoexchange.mobile.presentation.auth.resetpass

import android.os.Bundle
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ResetPasswordView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showEmailError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateTo(destinationId: Int, bundle: Bundle)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun navigateBack()
}