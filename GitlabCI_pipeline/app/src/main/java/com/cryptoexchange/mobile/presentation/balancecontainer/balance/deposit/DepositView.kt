package com.cryptoexchange.mobile.presentation.balancecontainer.balance.deposit

import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.BalanceTransactionType
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface DepositView : BaseView {

    @StateStrategyType(AddToEndStrategy::class)
    fun showReceiveAddress()

    @StateStrategyType(AddToEndStrategy::class)
    fun showReceiveMemo()

    @StateStrategyType(AddToEndStrategy::class)
    fun updateReceiveAddress(receiveAddress: String)

    @StateStrategyType(AddToEndStrategy::class)
    fun updateReceiveMemo(receiveMemo: String)

    @StateStrategyType(AddToEndStrategy::class)
    fun updateUIWithArguments(balanceTransactionType: BalanceTransactionType)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}
