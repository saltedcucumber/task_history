package com.cryptoexchange.mobile.presentation.more.profile.twofastatus

import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TwoFAStatusView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToTwoFADisabling(@IdRes actionId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}
