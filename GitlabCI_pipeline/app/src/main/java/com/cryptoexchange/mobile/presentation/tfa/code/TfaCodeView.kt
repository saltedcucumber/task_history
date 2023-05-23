package com.cryptoexchange.mobile.presentation.tfa.code

import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TfaCodeView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showManualKey(key: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openAuthenticatorOnMarket()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToFinish(@IdRes actionId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCodeError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCopyResult(message: String)
}
