package com.cryptoexchange.mobile.presentation.balancecontainer.balance

import android.util.Log
import com.cryptoexchange.source.entity.wallet.address.DepositAddressResponse
import android.content.Context
import com.cryptoexchange.source.entity.wallet.WalletModel
import com.cryptoexchange.source.extensions.getCurrencyName
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.domain.interactor.WalletInteractor
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.domain.entity.backendwrappers.CurrencyWrapper
import com.cryptoexchange.mobile.extensions.clearAndAddAll
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.*
import java.math.BigDecimal
import javax.inject.Inject

class BalancePresenter @Inject constructor(
    private val walletInteractor: WalletInteractor,
    private val context: Context,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<BalanceView>() {

    private val currencies = mutableListOf<WalletModel>()
    private var isHideZero = false
    private var searchQuery = ""

    override fun attachView(view: BalanceView?) {
        super.attachView(view)

        viewState.updateUiState(isHideZero, searchQuery)
        loadCurrencies()
    }

    fun onHideZeroSwitched(isChecked: Boolean) {
        isHideZero = isChecked
        updateBalance()
    }

    fun onSearchChanged(query: String) {
        searchQuery = query
        updateBalance()
    }

    private fun loadCurrencies() {
        walletInteractor
            .getWalletList()
            .subscribe(
                {
                    currencies.clearAndAddAll(it)
                    updateBalance()
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }

    private fun updateBalance() {
        val zeroFiltered = if (isHideZero) {
            currencies.filter { it.getTotal() > BigDecimal.ZERO }
        } else {
            currencies
        }

        val searchFiltered = if (searchQuery.isNotEmpty()) {
            zeroFiltered.filter {
                it.currencyShortName.contains(searchQuery, true) ||
                    getCurrencyName(context, it.currencyShortName).contains(searchQuery, true)
            }
        } else {
            zeroFiltered
        }

        viewState.showCurrencies(searchFiltered)
    }

    fun onDepositClicked(walletId: Long) {
        walletInteractor
            .getTransactionInfo(walletId)
            .map { resolveDepositType(it.first, it.second) }
            .subscribe(
                { viewState.navigateToDeposit(it) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Error during getting transaction info for deposit", it)
                }
            )
            .connect()
    }

    fun onWithdrawClicked(walletId: Long) {
        walletInteractor
            .getTransactionInfo(walletId)
            .map { resolveWithdrawType(it.first, it.second) }
            .subscribe(
                { viewState.navigateToWithdraw(it) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Error during getting transaction info for withdraw", it)
                }
            )
            .connect()
    }

    private fun resolveDepositType(
        depositInfo: DepositAddressResponse,
        currencyWrapper: CurrencyWrapper
    ): DepositType {
        return depositInfo.run {
            val tokensRemains = currencyWrapper.buildTokensRemainsString(resourceManager)
            if (depositInfo.memo.isNotEmpty()) {
                MemoDepositType(wallet, tokensRemains, address, memo)
            } else {
                DefaultDepositType(wallet, tokensRemains, address)
            }
        }
    }

    private fun resolveWithdrawType(
        depositInfo: DepositAddressResponse,
        currencyWrapper: CurrencyWrapper
    ): WithdrawType {
        return depositInfo.run {
            val tokensRemains = currencyWrapper.buildTokensRemainsString(resourceManager)
            if (depositInfo.memo.isNotEmpty()) {
                MemoWithdrawType(wallet, tokensRemains, currencyWrapper.id)
            } else {
                DefaultWithdrawType(wallet, tokensRemains, currencyWrapper.id)
            }
        }
    }

    companion object {

        private val TAG = BalancePresenter::class.java.simpleName
    }
}
