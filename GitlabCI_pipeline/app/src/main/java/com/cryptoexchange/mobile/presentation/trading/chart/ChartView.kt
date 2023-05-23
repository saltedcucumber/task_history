package com.cryptoexchange.mobile.presentation.trading.chart

import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.source.entity.trade.CandleModel
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ChartView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showChart(candleModels: List<CandleModel>)
}
