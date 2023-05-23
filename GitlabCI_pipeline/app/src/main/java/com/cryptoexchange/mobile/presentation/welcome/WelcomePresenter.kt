package com.cryptoexchange.mobile.presentation.welcome

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BasePresenter
import javax.inject.Inject

class WelcomePresenter @Inject constructor() : BasePresenter<WelcomeView>() {

    fun onLoginClicked() {
        viewState.navigateTo(R.id.action_welcomeFragment_to_loginFragment)
    }

    fun onSignUpClicked() {
        viewState.navigateTo(R.id.action_welcomeFragment_to_signUpFragment)
    }
}
