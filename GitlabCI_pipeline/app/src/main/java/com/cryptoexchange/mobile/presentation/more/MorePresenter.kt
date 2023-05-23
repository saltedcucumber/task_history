package com.cryptoexchange.mobile.presentation.more

import com.cryptoexchange.source.entity.user.UserKysStatus
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import javax.inject.Inject

class MorePresenter @Inject constructor(
    private val userInteractor: UserInteractor
) : BasePresenter<MoreView>() {

    private var isUserVerified = false

    override fun attachView(view: MoreView?) {
        super.attachView(view)

        handleUserVerificationStatus()
    }

    fun onLogoutClicked() {
        userInteractor
            .logout()
            .doAfterTerminate { viewState.restart() }
            .subscribe({}, {})
            .connect()
    }

    fun onActivityLogClicked() {
        viewState.navigateTo(R.id.activityLogFragment)
    }

    fun onDocumentsClicked() {
        viewState.inDevelopment()
    }

    fun onFaqClicked() {
        viewState.inDevelopment()
    }

    fun onProfileSettingsClicked() {
        viewState.navigateTo(R.id.action_moreInfoDestination_to_profileFragment)
    }

    fun onReferralsClicked() {
        viewState.inDevelopment()
    }

    fun onTradingHistoryClicked() {
        viewState.navigateTo(R.id.tradingHistoryFragment)
    }

    fun onTransactionHistoryClicked() {
        viewState.navigateTo(R.id.transactionHistoryFragment)
    }

    fun onVerificationClicked() {
        if (isUserVerified) {
            viewState.showMessage(R.string.user_verified, MessageType.SUCCESS)
        } else {
            viewState.navigateTo(R.id.verificationFragment)
        }
    }

    private fun handleUserVerificationStatus() {
        userInteractor
            .getUser()
            .subscribe(
                {
                    val kysStatus = it.userInfo.kysStatus
                    val messageId: Int
                    isUserVerified = kysStatus == UserKysStatus.VERIFIED_CORPORATE ||
                        kysStatus == UserKysStatus.VERIFIED_PERSONAL
                    val iconId = if (isUserVerified) {
                        messageId = R.string.verification_completed
                        R.drawable.ic_user_verification_status_sucess
                    } else {
                        messageId = R.string.verification_not_completed
                        R.drawable.ic_user_verification_status_failed
                    }
                    viewState.showUserVerificationStatus(iconId, messageId)
                },
                {
                    viewState.showUserVerificationStatus(
                        R.drawable.ic_user_verification_status_failed,
                        R.string.verification_not_completed
                    )
                }
            )
            .connect()
    }
}
