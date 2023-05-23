package com.cryptoexchange.mobile.presentation.tfa.enabletfa

import com.cryptoexchange.mobile.core.base.BasePresenter
import javax.inject.Inject

class EnableTfaPresenter @Inject constructor() : BasePresenter<EnableTfaView>() {

    fun onBackPressed() {
        viewState.navigateBack()
    }

    fun onContinuePressed() {
        viewState.navigateForward()
    }
}