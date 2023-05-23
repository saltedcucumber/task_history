package com.cryptoexchange.mobile.presentation.auth.success

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SuccessView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openEmail()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun navigateBack()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun registerOnBackPressedListener()
}