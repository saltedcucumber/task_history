package com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.cancellation

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.WalletInteractor
import javax.inject.Inject

class WithdrawCancellationPresenter @Inject constructor(
    private val walletInteractor: WalletInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<WithdrawCancellationView>() {

    private var withdrawId: Long = -1L

    fun setData(withdrawId: Long) {
        this.withdrawId = withdrawId
    }

    fun onProceedClicked(tfaCode: String) {
        val (isTfaCodeValid, tfaCodeError) = tfaCode.run {
            isNotEmpty() to resourceManager.getString(R.string.required_field)
        }

        if (isTfaCodeValid) {
            cancelWithdraw(withdrawId, tfaCode)
        } else {
            viewState.showTfaError(tfaCodeError)
        }
    }

    private fun cancelWithdraw(id: Long, twoFaCode: String) {
        walletInteractor
            .cancelWithdraw(id, twoFaCode)
            .subscribe(
                { viewState.backToBalance(R.id.balanceDestination) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }
}
