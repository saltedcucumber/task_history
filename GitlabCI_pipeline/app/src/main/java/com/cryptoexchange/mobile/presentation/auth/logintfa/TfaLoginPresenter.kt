package com.cryptoexchange.mobile.presentation.auth.logintfa

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import javax.inject.Inject

class TfaLoginPresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<TfaLoginView>() {

    private var isCreatePasswordFlow = false

    fun setData(isCreatePasswordFlow: Boolean) {
        this.isCreatePasswordFlow = isCreatePasswordFlow
    }

    fun onBackPressed() {
        userInteractor
            .logout()
            .doAfterTerminate(::handleBackNavigation)
            .subscribe({}, {})
            .connect()
    }

    fun onSignInClicked(code: String) {
        val isTfaPresent = code.isNotBlank()
        if (isTfaPresent) {
            signIn(code)
        }
        showInputError(isTfaPresent)
    }

    private fun signIn(code: String) {
        userInteractor
            .twoFaLogin(code)
            .subscribe(
                {
                    viewState.navigateTo(R.id.action_tfaLoginFragment_to_mainFragment)
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }

    private fun handleBackNavigation() {
        if (isCreatePasswordFlow) {
            viewState.navigateTo(R.id.action_tfaLoginFragment_to_welcomeFragment)
        } else {
            viewState.navigateBack()
        }
    }

    private fun showInputError(
        isTfaMissing: Boolean
    ) {
        val missingTfa =
            resourceManager.getErrorMessage(isTfaMissing, R.string.required_field)

        viewState.showMissingTwoFaError(missingTfa)
    }
}