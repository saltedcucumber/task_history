package com.cryptoexchange.mobile.presentation.dashboard

import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.mobile.domain.entity.CurrencyHistory
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface DashboardView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showAccountOverview()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showTfaStatus(isVerified: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showVerificationStatus(isVerified: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showEmail(email: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateTo(actionid: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCrrenciesRate(currenciesRate: List<CurrencyHistory>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun changeNavigationTab(tabId: Int)
}
