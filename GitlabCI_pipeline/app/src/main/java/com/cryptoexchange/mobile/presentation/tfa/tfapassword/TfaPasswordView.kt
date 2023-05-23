package com.cryptoexchange.mobile.presentation.tfa.tfapassword

import android.os.Bundle
import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.base.BaseView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TfaPasswordView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateTo(@IdRes actionId: Int, bundle: Bundle? = null)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateBack()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showPasswordError(message: String)
}
