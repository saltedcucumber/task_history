package com.cryptoexchange.mobile.presentation.more.profile

import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ProfileView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateTo(@IdRes actionId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}
