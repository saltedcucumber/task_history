package com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.cancellation

import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface WithdrawCancellationView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showTfaError(error: String?)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun backToBalance(@IdRes destinationId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}
