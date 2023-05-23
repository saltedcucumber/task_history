package com.cryptoexchange.mobile.app.presentation

import android.content.Intent
import com.cryptoexchange.mobile.BuildConfig
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.FailedNotification
import com.cryptoexchange.mobile.core.SuccessNotification
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.data.storage.Prefs
import com.cryptoexchange.mobile.domain.interactor.TfaInteractor
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import com.cryptoexchange.mobile.domain.interactor.WalletInteractor
import com.cryptoexchange.mobile.presentation.auth.createpass.CreatePasswordFragment
import com.cryptoexchange.source.entrypoint.deeplink.*
import com.cryptoexchange.source.entity.error.ErrorStatus
import com.cryptoexchange.source.entity.error.FailedRequestException
import javax.inject.Inject

class AppPresenter @Inject constructor(
    private val prefs: Prefs,
    private val userInteractor: UserInteractor,
    private val tfaInteractor: TfaInteractor,
    private val walletInteractor: WalletInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<AppView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        if (BuildConfig.DEBUG) {
            announceEnvironmentType()
        }
    }

    fun onNewIntent(intent: Intent) {
        val action = intent.action ?: ""

        if (action == Intent.ACTION_MAIN) {
            resolveStartDestination()
        } else {
            handleIntent(intent)
        }
    }

    fun onSessionExpired() {
        userInteractor
            .logout()
            .doAfterTerminate { viewState.navigateToWelcome() }
            .subscribe({}, {})
            .connect()
    }

    private fun announceEnvironmentType() {
        val type = prefs.envName
        viewState.showToast(type)
    }

    private fun resolveStartDestination() {
        userInteractor
            .getUser()
            .subscribe(
                {
                    val token = it.token
                    if (token.isNotBlank()) {
                        if (token.length <= TFA_TOKEN_LENGTH) {
                            logout()
                        } else {
                            viewState.setupStartDestination(R.id.mainFragment)
                        }
                    } else {
                        logout()
                    }
                },
                { viewState.setupStartDestination(R.id.welcomeFragment) }
            )
            .connect()
    }

    private fun logout() {
        userInteractor
            .logout()
            .doAfterTerminate { viewState.setupStartDestination(R.id.welcomeFragment) }
            .subscribe({}, {})
            .connect()
    }

    private fun handleIntent(intent: Intent) {
        when (val deepLinkAction = DeepLinkHandler.handle(intent)) {
            is DeepLinkConfirmEmail -> confirmEmail(deepLinkAction)
            is DeepLinkCreateNewPass -> createNewPass(deepLinkAction)
            is DeepLinkEnableTwoFa -> enableTfa(deepLinkAction)
            is DeepLinkDisableTwoFa -> twoFaConfirmDisable(deepLinkAction)
            DeepLinkNoAction,
            is DeepLinkError -> resolveStartDestination()
            is DeepLinkConfirmWithdraw -> confirmWithdraw(deepLinkAction)
        }
    }

    private fun confirmWithdraw(action: DeepLinkConfirmWithdraw) {
        walletInteractor
            .confirmWithdraw(action)
            .subscribe(
                {
                    with(viewState) {
                        showMessage(
                            resourceManager.getString(R.string.withdraw_confirmed),
                            SuccessNotification()
                        )
                        navigateToBalance(R.id.balanceDestination)
                    }
                },
                {
                    if (
                        it is FailedRequestException &&
                        it.errorStatus == ErrorStatus.WRONG_TX_STATUS
                    ) {
                        viewState.showMessage(it.message, FailedNotification())
                    } else {
                        errorHandler.handleError(it) { message ->
                            viewState.showMessage(message, FailedNotification())
                        }
                    }
                }
            )
    }

    private fun confirmEmail(action: DeepLinkConfirmEmail) {
        userInteractor
            .confirmEmail(action)
            .doAfterTerminate { resolveStartDestination() }
            .subscribe(
                {
                    val message = resourceManager.getString(R.string.success)
                    viewState.showMessage(message, SuccessNotification())
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, FailedNotification())
                    }
                }
            )
            .connect()
    }

    private fun createNewPass(action: DeepLinkCreateNewPass) {
        val bundle = CreatePasswordFragment.getBundle(action)
        viewState.navigateToCreatePassword(bundle)
    }

    private fun enableTfa(action: DeepLinkEnableTwoFa) {
        tfaInteractor
            .twoFaStartEnable(action)
            .doAfterTerminate { resolveStartDestination() }
            .subscribe(
                {
                    val message = resourceManager.getString(R.string.tfa_enabled)
                    viewState.showMessage(message, SuccessNotification())
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, FailedNotification())
                    }
                }
            )
            .connect()
    }

    private fun twoFaConfirmDisable(action: DeepLinkDisableTwoFa) {
        tfaInteractor
            .twoFaConfirmDisable(action)
            .doAfterTerminate { resolveStartDestination() }
            .subscribe(
                {
                    val message = resourceManager.getString(R.string.tfa_disabled)
                    viewState.showMessage(message, SuccessNotification())
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, FailedNotification())
                    }
                }
            )
            .connect()
    }

    companion object {
        const val TFA_TOKEN_LENGTH = 32
    }
}
