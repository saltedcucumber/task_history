package com.cryptoexchange.mobile.presentation.auth.resetpasssuccess

import com.cryptoexchange.mobile.core.base.BasePresenter
import javax.inject.Inject

class ResetPassSuccessPresenter @Inject constructor() :
    BasePresenter<ResetPassSuccessView>() {

    private lateinit var email: String

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.showSuccessMessage(email)
    }

    fun data(email: String) {
        this.email = email
    }

    fun onProceedClicked() {
        viewState.openEmail()
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }
}