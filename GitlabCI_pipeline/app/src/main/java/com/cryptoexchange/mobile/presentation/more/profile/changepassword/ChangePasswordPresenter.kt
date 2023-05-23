package com.cryptoexchange.mobile.presentation.more.profile.changepassword

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import com.cryptoexchange.source.entity.user.UserModel
import java.util.regex.Pattern
import javax.inject.Inject

class ChangePasswordPresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<ChangePasswordView>() {

    private val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])\\S{8,}\$")
    private lateinit var user: UserModel

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        handleTfaStatus()
    }

    fun onUpdateClicked(
        newPassword: String,
        newPasswordConfirm: String,
        oldPassword: String,
        tfaCode: String
    ) {
        val isOldPassword = passwordPattern.matcher(oldPassword).matches()
        val isNewPassword = passwordPattern.matcher(newPassword).matches()
        val isNewPasswordConfirm =
            newPassword == newPasswordConfirm && newPasswordConfirm.isNotEmpty()
        val isTfa = !user.userInfo.isGoogleAuthEnabled || tfaCode.isNotBlank()

        if (
            isOldPassword &&
            isNewPassword &&
            isNewPasswordConfirm &&
            isTfa
        ) {
            updatePassword(tfaCode, newPassword, oldPassword)
        }

        showInputError(isOldPassword, isNewPassword, isNewPasswordConfirm, isTfa)
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }

    private fun handleTfaStatus() {
        userInteractor
            .getUser()
            .subscribe(
                {
                    user = it
                    if (it.userInfo.isGoogleAuthEnabled) {
                        viewState.showTfaInputView()
                    }
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }

    private fun updatePassword(code: String, newPassword: String, oldPassword: String) {
        userInteractor
            .changePassword(code, newPassword, oldPassword)
            .subscribe(
                { viewState.navigateToSuccess() },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }

    private fun showInputError(
        isOldPassword: Boolean,
        isNewPassword: Boolean,
        isNewPasswordConfirm: Boolean,
        isTfa: Boolean
    ) {
        val oldPassword =
            resourceManager.getErrorMessage(isOldPassword, R.string.incorrect_password)
        val newPassword = resourceManager.getErrorMessage(isNewPassword, R.string.invalid_password)
        val newPasswordConfirm =
            resourceManager.getErrorMessage(isNewPasswordConfirm, R.string.password_not_match)
        val tfaMessage = resourceManager.getErrorMessage(isTfa, R.string.required_field)

        viewState.showOldPasswordError(oldPassword)
        viewState.showNewPasswordError(newPassword)
        viewState.showConfirmNewPasswordError(newPasswordConfirm)
        viewState.showTfaCodeError(tfaMessage)
    }
}