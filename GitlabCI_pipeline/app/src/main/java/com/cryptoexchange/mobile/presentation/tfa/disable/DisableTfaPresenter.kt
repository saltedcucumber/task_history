package com.cryptoexchange.mobile.presentation.tfa.disable

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.TfaInteractor
import javax.inject.Inject

class DisableTfaPresenter @Inject constructor(
    private val tfaInteractor: TfaInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<DisableTfaView>() {

    fun onDisableClicked(password: String, code: String) {
        val isPasswordError = password.isBlank()
        val isCodeError = code.isBlank()
        val passwordMessage =
            resourceManager.getErrorMessage(!isPasswordError, R.string.required_field)
        val codeMessage = resourceManager.getErrorMessage(!isCodeError, R.string.required_field)

        viewState.showErrorMessages(passwordMessage, codeMessage)

        if (!isCodeError && !isPasswordError) {
            sendDisableEmail(password, code)
        }
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }

    private fun sendDisableEmail(password: String, code: String) {
        tfaInteractor
            .twoFaSendDisableEmail(password, code)
            .subscribe(
                {
                    viewState.navigateToDisableSuccess(
                        R.id.action_disableTfaFragment_to_disableTfaSuccessFragment
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
