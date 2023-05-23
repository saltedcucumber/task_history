package com.cryptoexchange.mobile.app.presentation

import android.os.Bundle
import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.Notification
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface AppView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(text: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setupStartDestination(id: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToCreatePassword(bundle: Bundle)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToWelcome()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String, notification: Notification)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToBalance(@IdRes destinationId: Int)
}
