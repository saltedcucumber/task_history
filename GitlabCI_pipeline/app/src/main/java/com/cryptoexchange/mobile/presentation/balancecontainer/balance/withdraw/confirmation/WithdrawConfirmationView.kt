package com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.confirmation

import android.os.Bundle
import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface WithdrawConfirmationView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openEmail()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToWithdrawCancellation(@IdRes actionId: Int, bundle: Bundle)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()
}
