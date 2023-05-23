package com.cryptoexchange.mobile.presentation.main

import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MainView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupBottomNavigation(@IdRes startDestinations: IntArray)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun suggestTfa(@IdRes actionId: Int)
}
