package com.cryptoexchange.mobile.presentation.balancecontainer.balance

import com.cryptoexchange.source.entity.wallet.WalletModel
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.DepositType
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.WithdrawType

interface BalanceView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToDeposit(depositType: DepositType)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToWithdraw(withdrawType: WithdrawType)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCurrencies(currencies: List<WalletModel>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateUiState(isHideZero: Boolean, searchQuery: String)
}
