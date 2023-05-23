package com.cryptoexchange.mobile.presentation.main

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.TfaInteractor
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val tfaInteractor: TfaInteractor,
    private val errorHandler: ErrorHandler,
    private val userInteractor: UserInteractor
) : BasePresenter<MainView>() {

    private val startDestinations = intArrayOf(
        R.id.dashboardDestination,
        R.id.exchangeDestination,
        R.id.tradingDestination,
        R.id.balanceDestination,
        R.id.moreInfoDestination
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        checkIfTfaSuggested()
        updateUserInfo()
    }

    private fun checkIfTfaSuggested() {
        tfaInteractor
            .isTfaSuggested()
            .subscribe(
                {
                    if (!it) {
                        viewState.suggestTfa(R.id.action_mainFragment_to_two_fa_enabling_graph)
                        setSuggested()
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

    private fun setSuggested() {
        tfaInteractor
            .setSuggested()
            .subscribe({}, {})
            .connect()
    }

    private fun updateUserInfo() {
        userInteractor
            .getUserSettings()
            .doAfterTerminate { viewState.setupBottomNavigation(startDestinations) }
            .subscribe(
                {},
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }
}
