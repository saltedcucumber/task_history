package com.cryptoexchange.mobile.presentation.auth.login

import android.os.Bundle
import android.util.Patterns
import com.cryptoexchange.source.entity.user.UserModel
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import com.cryptoexchange.mobile.presentation.auth.logintfa.TfaLoginFragment
import com.cryptoexchange.source.entity.error.ErrorStatus
import com.cryptoexchange.source.entity.error.FailedRequestException
import java.util.regex.Pattern
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<LoginView>() {

    private val emailPattern = Patterns.EMAIL_ADDRESS
    private val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])\\S{8,}\$")

    fun onLoginClicked(email: String, password: String) {
        val isEmail = emailPattern.matcher(email).matches()
        val isPassword = passwordPattern.matcher(password).matches()

        if (isEmail && isPassword) {
            login(email, password)
        } else {
            showErrorMessages(isEmail, isPassword)
        }
    }

    fun onForgotPasswordClicked() {
        viewState.navigateTo(R.id.resetPasswordFragment, null)
    }

    fun onSignUpClicked() {
        viewState.navigateTo(R.id.action_loginFragment_to_signUpFragment, null)
    }

    private fun login(
        email: String,
        password: String
    ) {
        userInteractor
            .login(email, password)
            .subscribe(::handleLoginResult) {
                if (it is FailedRequestException && it.errorStatus == ErrorStatus.USER_NOT_FOUND) {
                    val message = resourceManager.getString(R.string.invalid_email_or_password)
                    viewState.showMessage(message, MessageType.FAILED)
                } else {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            }
            .connect()
    }

    private fun showErrorMessages(
        isEmail: Boolean,
        isPassword: Boolean
    ) {
        val emailError = if (!isEmail) {
            resourceManager.getString(R.string.invalid_email)
        } else {
            ""
        }
        val passwordError = if (!isPassword) {
            resourceManager.getString(R.string.invalid_password)
        } else {
            ""
        }

        viewState.showEmailError(emailError)
        viewState.showPasswordError(passwordError)
    }

    private fun handleLoginResult(user: UserModel) {
        if (user.userInfo.isEmailConfirmed) {
            var bundle: Bundle? = null
            val destination = if (user.userInfo.isGoogleAuthEnabled) {
                bundle = TfaLoginFragment.getBundle(false)
                R.id.tfaLoginFragment
            } else {
                R.id.action_loginFragment_to_mainFragment
            }
            viewState.navigateTo(destination, bundle)
        } else {
            viewState.showMessage(R.string.email_not_confirmed, MessageType.FAILED)
        }
    }
}
