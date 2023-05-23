package com.cryptoexchange.mobile.presentation.tfa.tfapassword

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.TfaInteractor
import com.cryptoexchange.mobile.presentation.tfa.backupkey.TfaBackupKeyFragment
import retrofit2.HttpException
import javax.inject.Inject

class TfaPasswordPresenter @Inject constructor(
    private val tfaInteractor: TfaInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<TfaPasswordView>() {

    fun onAboutTfaClicked() {
        viewState.navigateTo(R.id.action_tfaPasswordFragment_to_aboutTfaFragment)
    }

    fun onStartClicked(password: String) {
        tfaInteractor
            .twoFaStartEnable(password)
            .subscribe(
                {
                    viewState.navigateTo(
                        R.id.action_tfaPasswordFragment_to_tfaBackupKeyFragment,
                        TfaBackupKeyFragment.getBundle(it.secret)
                    )
                },
                {
                    if (it is HttpException) {
                        val message = resourceManager.getString(R.string.incorrect_password)
                        viewState.showPasswordError(message)
                    } else {
                        errorHandler.handleError(it) { message ->
                            viewState.showMessage(message, MessageType.FAILED)
                        }
                    }
                }
            )
            .connect()
    }

    fun onBackClicked() {
        viewState.navigateBack()
    }
}