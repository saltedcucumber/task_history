package com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw

import android.os.Bundle
import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.WithdrawType
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface WithdrawView : BaseView {

    @StateStrategyType(AddToEndStrategy::class)
    fun showMemoInput()

    @StateStrategyType(AddToEndStrategy::class)
    fun updateUIWithArguments(withdrawType: WithdrawType)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateWithdrawConfirmation(@IdRes actionId: Int, bundle: Bundle)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showAmountError(error: String?)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showReceiveAddressError(error: String?)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMemoError(error: String?)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showTfaError(error: String?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showFee(fee: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}
