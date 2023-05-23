package com.cryptoexchange.mobile.presentation.auth.login

import android.os.Bundle
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface LoginView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showEmailError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showPasswordError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateTo(destinationId: Int, bundle: Bundle?)
}
