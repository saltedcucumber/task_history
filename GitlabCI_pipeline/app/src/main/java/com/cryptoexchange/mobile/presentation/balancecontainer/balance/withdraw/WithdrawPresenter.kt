package com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw

import android.util.Log
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.interactor.ExchangeInteractor
import com.cryptoexchange.mobile.domain.interactor.WalletInteractor
import com.cryptoexchange.mobile.extensions.smaller
import com.cryptoexchange.mobile.extensions.toDisplayStringBy
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.MemoWithdrawType
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.WithdrawType
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.confirmation.WithdrawConfirmationFragment
import com.cryptoexchange.source.entity.withdraw.PayGateType
import java.math.BigDecimal
import javax.inject.Inject

class WithdrawPresenter @Inject constructor(
    private val walletInteractor: WalletInteractor,
    private val exchangeInteractor: ExchangeInteractor,
    private val resourceManager: ResourceManager,
    private val errorHandler: ErrorHandler
) : BasePresenter<WithdrawView>() {

    private lateinit var withdrawType: WithdrawType

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        updateUIByDepositType()
        loadFee()
    }

    fun data(withdrawType: WithdrawType) {
        this.withdrawType = withdrawType
    }

    fun onProceedClicked(
        amountString: String,
        receiveAddress: String,
        tfaCode: String,
        memo: String?
    ) {
        val (isAmountValid, amountError) = amountString.validateAmount()

        val (isReceiveAddressValid, addressError) = receiveAddress.run {
            isNotEmpty() to resourceManager.getString(R.string.required_field)
        }

        val (isTfaCodeValid, tfaCodeError) = tfaCode.run {
            isNotEmpty() to resourceManager.getString(R.string.required_field)
        }

        val (isMemoValid, memoError) = memo.run {
            (this?.isNotEmpty() ?: true) to resourceManager.getString(R.string.required_field)
        }

        if (isAmountValid && isReceiveAddressValid && isTfaCodeValid && isMemoValid) {
            requestWithdraw(
                amountString.toBigDecimal(),
                withdrawType.currencyId,
                memo,
                PayGateType.CRYPTO,
                receiveAddress,
                tfaCode
            )
        } else {
            if (!isAmountValid) {
                viewState.showAmountError(amountError)
            }
            if (!isReceiveAddressValid) {
                viewState.showReceiveAddressError(addressError)
            }
            if (!isTfaCodeValid) {
                viewState.showTfaError(tfaCodeError)
            }
            if (!isMemoValid) {
                viewState.showMemoError(memoError)
            }
        }
    }

    private fun String.validateAmount(): Pair<Boolean, String?> {
        val (hasRestriction, minValue) = walletInteractor
            .getAmountRestrictions(withdrawType.currencyId) ?: Pair(false, null)

        val amount = this.toBigDecimalOrNull()
        val error = when {
            isEmpty() -> {
                resourceManager.getString(R.string.required_field)
            }
            amount != null && amount <= BigDecimal.ZERO -> {
                resourceManager.getString(R.string.must_be_greater_than_zero)
            }
            hasRestriction && minValue != null && amount.smaller(minValue) -> {
                resourceManager.getString(
                    R.string.must_be_greater_than_pattern,
                    minValue.toDisplayStringBy()
                )
            }
            else -> null
        }
        val isValid = error == null
        return isValid to error
    }

    private fun requestWithdraw(
        amount: BigDecimal,
        currencyId: Long,
        memo: String?,
        payGateType: PayGateType,
        receivingAddress: String,
        twoFaCode: String
    ) {
        walletInteractor
            .requestWithdraw(amount, currencyId, memo, payGateType, receivingAddress, twoFaCode)
            .subscribe(
                {
                    viewState.navigateWithdrawConfirmation(
                        R.id.action_withdrawFragment_to_withdrawConfirmationFragment,
                        WithdrawConfirmationFragment.getBundle(it.id)
                    )
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Error during requesting withdraw", it)
                }
            )
            .connect()
    }

    private fun updateUIByDepositType() {
        with(withdrawType) {
            when (this) {
                is MemoWithdrawType -> viewState.showMemoInput()
                else -> {}
            }
            viewState.updateUIWithArguments(this)
        }
    }

    private fun loadFee() {
        exchangeInteractor
            .loadCurrencies()
            .subscribe(
                { currencies ->
                    val currency = currencies.find { it.id == withdrawType.currencyId }
                    currency?.let {
                        val fee = String.format(
                            FEE_PATTERN,
                            it.withdrawFee.toDisplayStringBy(),
                            it.shortName
                        )
                        viewState.showFee(fee)
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

    fun onBackPressed() {
        viewState.navigateBack()
    }

    companion object {
        private const val FEE_PATTERN = "%s %s"

        private val TAG = WithdrawPresenter::class.java.simpleName
    }
}
