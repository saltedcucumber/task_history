package com.cryptoexchange.mobile.presentation.dashboard

import android.text.format.DateUtils
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.ExchangeInteractor
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import com.cryptoexchange.mobile.extensions.THIRTY_DAYS
import com.cryptoexchange.mobile.extensions.YYYY_MM_DD
import com.cryptoexchange.source.entity.user.UserKysStatus
import com.cryptoexchange.source.entity.user.UserModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DashboardPresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler,
    private val exchangeInteractor: ExchangeInteractor
) : BasePresenter<DashboardView>() {

    private lateinit var user: UserModel

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadCurrenciesRate()
    }

    override fun attachView(view: DashboardView?) {
        super.attachView(view)

        loadUser()
    }

    fun onDropdownClicked() {
        viewState.showAccountOverview()
    }

    fun onTfaClicked() {
        val actionId = if (user.userInfo.isGoogleAuthEnabled) {
            R.id.twoFAStatusFragment
        } else {
            R.id.action_dashboardDestination_to_two_fa_enabling_graph
        }
        viewState.navigateTo(actionId)
    }

    fun onDepositClicked() {
        viewState.changeNavigationTab(R.id.balanceDestination)
    }

    fun onExchangeClicked() {
        viewState.changeNavigationTab(R.id.exchangeDestination)
    }

    fun onNewsClicked() {
        viewState.navigateTo(R.id.newsFragment)
    }

    private fun loadUser() {
        userInteractor
            .getUser()
            .subscribe(
                {
                    user = it
                    val kysStatus = it.userInfo.kysStatus
                    val isVerified = kysStatus == UserKysStatus.VERIFIED_CORPORATE ||
                        kysStatus == UserKysStatus.VERIFIED_PERSONAL
                    viewState.showTfaStatus(it.userInfo.isGoogleAuthEnabled)
                    viewState.showVerificationStatus(isVerified)
                    viewState.showEmail(it.userInfo.email)
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }

    private fun loadCurrenciesRate() {
        val todayMills = System.currentTimeMillis()
        val monthAgoMills = todayMills - DateUtils.DAY_IN_MILLIS * THIRTY_DAYS
        val dateFormat = SimpleDateFormat(YYYY_MM_DD, Locale.getDefault())
        val dateFrom = dateFormat.format(Date(monthAgoMills))
        val dateTo = dateFormat.format(Date(todayMills))

        exchangeInteractor
            .getExchangeHistory(dateFrom, dateTo)
            .subscribe(
                { viewState.showCrrenciesRate(it) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }
}
