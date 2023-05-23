package com.cryptoexchange.mobile.presentation.auth.signup

import android.util.Patterns
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import java.util.regex.Pattern
import javax.inject.Inject

class SignUpPresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<SignUpView>() {

    private val emailPattern = Patterns.EMAIL_ADDRESS
    private val passwordPattern = Pattern.compile(PASSWORD_PATTERN)

    fun onSignUpClicked(
        referralCode: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String,
        isTermsAgreed: Boolean
    ) {
        val isFirstName = firstName.isNotBlank()
        val isLastName = lastName.isNotBlank()
        val isEmail = emailPattern.matcher(email).matches()
        val isPassword = passwordPattern.matcher(password).matches()
        val isConfirmPassword = password == confirmPassword && confirmPassword.isNotEmpty()

        if (
            isFirstName &&
            isLastName &&
            isEmail &&
            isPassword &&
            isConfirmPassword &&
            isTermsAgreed
        ) {
            register(firstName, lastName, email, password, referralCode)
        } else {
            showErrorMessages(
                isLastName,
                isFirstName,
                isEmail,
                isPassword,
                isConfirmPassword,
                isTermsAgreed
            )
        }
    }

    fun onTermsAndConditionsClicked() {
        viewState.navigateTo(R.id.termsAndConditionsFragment)
    }

    fun onBackClicked() {
        viewState.navigateBack()
    }

    private fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        referralCode: String
    ) {
        userInteractor
            .register(firstName, lastName, email, password, referralCode)
            .subscribe(
                { viewState.navigateTo(R.id.action_signUpFragment_to_signUpSuccessFragment) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }

    private fun showErrorMessages(
        isLastName: Boolean,
        isFirstName: Boolean,
        isEmail: Boolean,
        isPassword: Boolean,
        isConfirmPassword: Boolean,
        isTermsAgreed: Boolean
    ) {
        val firstNameError = resourceManager.getErrorMessage(isFirstName, R.string.required_field)
        val lastNameError = resourceManager.getErrorMessage(isLastName, R.string.required_field)
        val emailError = resourceManager.getErrorMessage(isEmail, R.string.invalid_email)
        val passwordError = resourceManager.getErrorMessage(isPassword, R.string.invalid_password)
        val confirmPasswordError =
            resourceManager.getErrorMessage(isConfirmPassword, R.string.passwords_do_not_match)

        with(viewState) {
            showFirstNameError(firstNameError)
            showLastNameError(lastNameError)
            showEmailError(emailError)
            showPasswordError(passwordError)
            showConfirmPasswordError(confirmPasswordError)
        }

        if (!isTermsAgreed) {
            viewState.showMessage(R.string.terms_conditions_error_message, MessageType.FAILED)
        }
    }

    companion object {
        private const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])\\S{8,}\$"
    }
}
