package com.cryptoexchange.mobile.presentation.tfa.finished

import com.cryptoexchange.mobile.core.base.BasePresenter
import javax.inject.Inject

class TfaFinishedPresenter @Inject constructor() : BasePresenter<TfaFinishedView>() {

    fun onBackPressed() {
        viewState.navigateBack()
    }

    fun onProceedClicked() {
        viewState.endTfaFlow()
    }
}
