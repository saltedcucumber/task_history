package com.cryptoexchange.mobile.presentation.more.transactionhistory

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import com.cryptoexchange.source.entity.withdraw.request.MoneyTransactionModel
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TransactionHistoryView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTransactions(transactions: List<MoneyTransactionModel>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showFilter(@IdRes actionId: Int, bundle: Bundle? = null)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateFilterIcon(@DrawableRes drawableRes: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateWithdrawConfirmation(@IdRes actionId: Int, bundle: Bundle? = null)
}
