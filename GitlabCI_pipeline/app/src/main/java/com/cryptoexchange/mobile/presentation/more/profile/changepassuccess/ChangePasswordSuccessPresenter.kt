package com.cryptoexchange.mobile.presentation.more.profile.changepassuccess

import com.cryptoexchange.mobile.core.base.BasePresenter
import javax.inject.Inject

class ChangePasswordSuccessPresenter @Inject constructor() :
    BasePresenter<ChangePasswordSuccessView>() {

    fun onLogOutClicked() {
        viewState.restart()
    }
}