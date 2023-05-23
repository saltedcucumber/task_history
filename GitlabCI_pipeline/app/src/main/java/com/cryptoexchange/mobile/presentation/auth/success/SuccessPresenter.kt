package com.cryptoexchange.mobile.presentation.auth.success

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import com.cryptoexchange.source.entity.user.UserModel
import javax.inject.Inject

class SuccessPresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<SuccessView>() {

    private lateinit var user: UserModel

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        getUser()
        viewState.registerOnBackPressedListener()
    }

    fun onContinueClicked() {
        viewState.openEmail()
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }

    fun onResendClicked() {
        userInteractor
            .resendEmail(user.userInfo.email)
            .subscribe(
                {
                    viewState.showMessage(
                        resourceManager.getString(R.string.email_sent),
                        MessageType.SUCCESS
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

    private fun getUser() {
        userInteractor
            .getUser()
            .subscribe(
                {
                    user = it
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