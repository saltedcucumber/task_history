package com.cryptoexchange.mobile.presentation.tfa.disablesuccess

import com.cryptoexchange.mobile.core.base.BasePresenter
import javax.inject.Inject

class DisableTfaSuccessPresenter @Inject constructor() : BasePresenter<DisableTfaSuccessView>() {

    fun onBackPressed() {
        viewState.navigateBack()
    }

    fun onProceedClicked() {
        viewState.openEmail()
    }
}