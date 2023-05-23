package com.cryptoexchange.mobile.presentation.more.activitylog

import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import javax.inject.Inject

class ActivityLogPresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<ActivityLogView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadUserActivities()
    }

    fun onSwipeToRefresh() {
        loadUserActivities()
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }

    private fun loadUserActivities() {
        userInteractor
            .getUserActivities()
            .subscribe(
                { viewState.showUserActivities(it.activities) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }
}