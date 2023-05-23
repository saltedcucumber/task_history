package com.cryptoexchange.mobile.presentation.more.transactionhistory

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.ExchangeInteractor
import com.cryptoexchange.mobile.domain.interactor.WalletInteractor
import com.cryptoexchange.mobile.extensions.clearAndAddAll
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.confirmation.WithdrawConfirmationFragment
import com.cryptoexchange.mobile.presentation.more.transactionhistory.filter.TransactionFilterSettings
import com.cryptoexchange.mobile.presentation.more.transactionhistory.filter.TransactionHistoryFilterFragment
import com.cryptoexchange.source.entity.wallet.Page
import com.cryptoexchange.source.entity.wallet.TransactionsModel
import com.cryptoexchange.source.entity.wallet.transaction.TransactionQueries
import com.cryptoexchange.source.entity.withdraw.request.MoneyTransactionModel
import javax.inject.Inject

class TransactionHistoryPresenter @Inject constructor(
    private val walletInteractor: WalletInteractor,
    private val exchangeInteractor: ExchangeInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<TransactionHistoryView>() {

    private val transactions = mutableListOf<MoneyTransactionModel>()
    private var filterSettings: TransactionFilterSettings? = null
    private var isLoadInProgress = false
    private lateinit var page: Page

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadNewPage(TransactionQueries(INITIAL_PAGE))
    }

    fun onFilterClicked() {
        viewState.showFilter(
            R.id.action_transactionHistoryFragment_to_transactionHistoryFilterFilterFragment,
            filterSettings?.let { TransactionHistoryFilterFragment.getBundle(it) }
        )
    }

    fun onWithdrawConfirmationNeeded(walletId: Long) {
        viewState.navigateWithdrawConfirmation(
            R.id.action_transactionHistoryFragment_to_withdrawConfirmationFragment,
            WithdrawConfirmationFragment.getBundle(walletId)
        )
    }

    fun applyFilter(settings: TransactionFilterSettings?) {
        if (settings != filterSettings) {
            filterSettings = settings
            val currencyIds = exchangeInteractor.cachedCurrencies
                .filter { currency -> currency.shortName.uppercase() == settings?.currency }
                .map { it.id }
            loadNewPage(
                TransactionQueries(
                    page = INITIAL_PAGE,
                    currencyIds = currencyIds,
                    dateFrom = settings?.dateFromBackendFormatted,
                    dateTo = settings?.dateToBackendFormatted,
                    type = settings?.typeBackend,
                )
            )
        }
        val icon = if (settings == null) {
            R.drawable.ic_filter
        } else {
            R.drawable.ic_filter_applied
        }
        viewState.updateFilterIcon(icon)
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }

    fun onNewPageNeed() {
        val currentPage = page.pageNumber
        val totalPages = page.totalPages

        if ((currentPage + 1) < totalPages) {
            val currencyIds = exchangeInteractor.cachedCurrencies
                .filter { currency -> currency.shortName.uppercase() == filterSettings?.currency }
                .map { it.id }
            loadNewPage(
                TransactionQueries(
                    page = currentPage + 1,
                    currencyIds = currencyIds,
                    dateFrom = filterSettings?.dateFromBackendFormatted,
                    dateTo = filterSettings?.dateToBackendFormatted,
                    type = filterSettings?.typeBackend,
                )
            )
        }
    }

    private fun loadNewPage(queries: TransactionQueries) {
        if (!isLoadInProgress) {
            walletInteractor
                .getTransactions(queries)
                .doOnSubscribe { isLoadInProgress = true }
                .doAfterTerminate { isLoadInProgress = false }
                .subscribe(::handleTransactions) {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
                .connect()
        }
    }

    private fun handleTransactions(model: TransactionsModel) {
        page = model.page
        if (page.pageNumber == INITIAL_PAGE) {
            transactions.clearAndAddAll(model.transactions)
        } else {
            transactions.addAll(model.transactions)
        }
        viewState.showTransactions(transactions)
    }

    companion object {
        private const val INITIAL_PAGE = 0
    }
}
