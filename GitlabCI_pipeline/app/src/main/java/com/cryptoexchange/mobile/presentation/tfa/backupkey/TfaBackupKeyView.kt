package com.cryptoexchange.mobile.presentation.tfa.backupkey

import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TfaBackupKeyView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showBackupKey(key: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToTfaCodeScreen(@IdRes actionId: Int, key: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCopyResult(message: String)
}
