package com.cryptoexchange.mobile.presentation.more.tradinghistory

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.mobile.domain.entity.TradingHistory
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TradingHistoryView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTradings(tradings: List<TradingHistory>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showDetails(@IdRes actionId: Int, bundle: Bundle? = null)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showFilter(@IdRes actionId: Int, bundle: Bundle? = null)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateFilterIcon(@DrawableRes drawableRes: Int)
}