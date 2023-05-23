package com.cryptoexchange.mobile.presentation.auth.createpass

import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.source.entrypoint.deeplink.DeepLinkCreateNewPass
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import java.util.regex.Pattern
import javax.inject.Inject

class CreatePasswordPresenter @Inject constructor(
    private val resourceManager: ResourceManager,
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<CreatePasswordView>() {

    private lateinit var deepLinkAction: DeepLinkCreateNewPass

    private val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])\\S{8,}\$")

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.registerOnBackPressedListener()
    }

    fun data(deepLinkAction: DeepLinkCreateNewPass) {
        this.deepLinkAction = deepLinkAction
    }

    fun onCreatePasswordClicked(password: String, confirmPassword: String) {
        val isPassword = passwordPattern.matcher(password).matches()
        val isConfirmPassword = password == confirmPassword

        if (isPassword && isConfirmPassword) {
            createNewPassword(password)
        } else {
            showErrors(isPassword, isConfirmPassword)
        }
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }

    private fun createNewPassword(password: String) {
        userInteractor
            .createNewPassword(deepLinkAction.email, password, deepLinkAction.token)
            .subscribe(
                { logout() },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            ).connect()
    }

    private fun showErrors(isPassword: Boolean, isConfirmPassword: Boolean) {
        val passwordMessage = if (isPassword) {
            ""
        } else {
            resourceManager.getString(R.string.invalid_password)
        }

        viewState.showPasswordError(passwordMessage)

        val confirmPassMessage = if (isConfirmPassword) {
            ""
        } else {
            resourceManager.getString(R.string.password_not_match)
        }

        viewState.showConfirmPasswordError(confirmPassMessage)
    }

    private fun logout() {
        userInteractor
            .logout()
            .doAfterTerminate { viewState.restart() }
            .subscribe({}, {})
            .connect()
    }
}