package com.cryptoexchange.mobile.presentation.dashboard.news

import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface NewsView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showNews(news: List<Unit>)
}