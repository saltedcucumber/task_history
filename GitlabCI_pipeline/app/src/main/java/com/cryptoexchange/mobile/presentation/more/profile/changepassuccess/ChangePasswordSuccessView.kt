package com.cryptoexchange.mobile.presentation.more.profile.changepassuccess

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ChangePasswordSuccessView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun restart()
}