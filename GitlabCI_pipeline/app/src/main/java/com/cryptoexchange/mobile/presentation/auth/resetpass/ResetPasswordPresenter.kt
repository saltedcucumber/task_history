package com.cryptoexchange.mobile.presentation.auth.resetpass

import android.util.Patterns
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import com.cryptoexchange.mobile.presentation.auth.resetpasssuccess.ResetPassSuccessFragment
import javax.inject.Inject

class ResetPasswordPresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<ResetPasswordView>() {

    private val emailPattern = Patterns.EMAIL_ADDRESS

    fun onResetClicked(email: String) {
        var errorMessage = ""

        errorMessage = if (emailPattern.matcher(email).matches()) {
            resetPassword(email)
            ""
        } else {
            resourceManager.getString(R.string.invalid_email)
        }
        viewState.showEmailError(errorMessage)
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }

    private fun resetPassword(email: String) {
        userInteractor
            .resetPassword(email)
            .subscribe(
                {
                    viewState.navigateTo(
                        R.id.action_resetPasswordFragment_to_resetPassSuccessFragment,
                        ResetPassSuccessFragment.getBundle(email)
                    )
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }
}