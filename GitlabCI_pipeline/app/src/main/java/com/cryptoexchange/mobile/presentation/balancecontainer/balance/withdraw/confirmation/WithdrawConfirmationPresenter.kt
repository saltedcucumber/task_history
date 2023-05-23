package com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.confirmation

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.WalletInteractor
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.cancellation.WithdrawCancellationFragment
import com.cryptoexchange.source.entity.error.ErrorStatus
import com.cryptoexchange.source.entity.error.FailedRequestException
import javax.inject.Inject

class WithdrawConfirmationPresenter @Inject constructor(
    private val walletInteractor: WalletInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<WithdrawConfirmationView>() {

    private var withdrawId: Long = -1L

    fun setData(withdrawId: Long) {
        this.withdrawId = withdrawId
    }

    fun onMailboxClicked() {
        viewState.openEmail()
    }

    fun onResendClicked() {
        walletInteractor
            .resendWithdrawConfirmationEmail(withdrawId)
            .subscribe(
                { viewState.showMessage(R.string.success, MessageType.SUCCESS) },
                ::onTooOftenRequest
            )
            .connect()
    }

    fun onCancelClicked() {
        viewState.navigateToWithdrawCancellation(
            R.id.action_withdrawConfirmationFragment_to_withdrawCancellationFragment,
            WithdrawCancellationFragment.getBundle(withdrawId)
        )
    }

    private fun showTooOftenRequestMessage() {
        viewState.showMessage(
            R.string.withdraw_confirmation_resend_time_restriction,
            MessageType.WARNING
        )
    }

    private fun onTooOftenRequest(it: Throwable) {
        if (it is FailedRequestException && it.errorStatus == ErrorStatus.TOO_OFTEN) {
            showTooOftenRequestMessage()
        } else {
            errorHandler.handleError(it) { message ->
                viewState.showMessage(message, MessageType.FAILED)
            }
        }
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }
}
