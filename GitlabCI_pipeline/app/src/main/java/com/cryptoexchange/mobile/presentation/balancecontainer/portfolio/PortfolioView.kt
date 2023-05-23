package com.cryptoexchange.mobile.presentation.balancecontainer.portfolio

import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.mobile.domain.entity.CurrencyInBasic
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.math.BigDecimal

interface PortfolioView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showPortfolio(currenciesInBasic: List<CurrencyInBasic>, totalBalance: BigDecimal)
}
