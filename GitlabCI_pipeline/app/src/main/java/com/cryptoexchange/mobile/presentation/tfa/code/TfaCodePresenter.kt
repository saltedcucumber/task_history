package com.cryptoexchange.mobile.presentation.tfa.code

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.TfaInteractor
import javax.inject.Inject

class TfaCodePresenter @Inject constructor(
    private val resourceManager: ResourceManager,
    private val tfaInteractor: TfaInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<TfaCodeView>() {

    private lateinit var manualKey: String

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.showManualKey(manualKey)
    }

    fun data(key: String) {
        manualKey = key
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }

    fun onCopyClicked() {
        val result = resourceManager.saveToClipboard(manualKey)
        val resultMessageId = if (result) {
            R.string.copied
        } else {
            R.string.copy_failed
        }

        viewState.showCopyResult(resourceManager.getString(resultMessageId))
    }

    fun onOpenPlayMarketClicked() {
        viewState.openAuthenticatorOnMarket()
    }

    fun onTfaEnableClicked(code: String) {
        val message = if (code.isBlank()) {
            resourceManager.getString(R.string.required_field)
        } else {
            sendConfirmEmail(code)
            ""
        }
        viewState.showCodeError(message)
    }

    private fun sendConfirmEmail(code: String) {
        tfaInteractor
            .twoFaSendConfirmEmail(code)
            .subscribe(
                { viewState.navigateToFinish(R.id.action_tfaCodeFragment_to_tfaFinishedFragment) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }
}