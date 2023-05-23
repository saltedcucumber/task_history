package com.cryptoexchange.mobile.presentation.auth.createpass

import android.os.Bundle
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface CreatePasswordView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showPasswordError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showConfirmPasswordError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateTo(actionId: Int, bundle: Bundle?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun navigateBack()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun registerOnBackPressedListener()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun restart()
}