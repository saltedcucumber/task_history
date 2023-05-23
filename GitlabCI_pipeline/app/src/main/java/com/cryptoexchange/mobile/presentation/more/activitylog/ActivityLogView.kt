package com.cryptoexchange.mobile.presentation.more.activitylog

import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.source.entity.user.activities.UserActivityModel
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ActivityLogView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showUserActivities(activities: List<UserActivityModel>)
}