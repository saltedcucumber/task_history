package com.cryptoexchange.mobile.presentation.more.transactionhistory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptoexchange.source.entity.withdraw.request.MoneyTransactionModel
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentTransactionHistoryBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.observeBackStackSavedState
import com.cryptoexchange.mobile.extensions.visibleUnless
import com.cryptoexchange.mobile.presentation.more.transactionhistory.adapter.TransactionHistoryAdapter
import com.cryptoexchange.mobile.presentation.more.transactionhistory.adapter.TransactionsAdapterScrollListener
import com.cryptoexchange.mobile.presentation.more.transactionhistory.adapter.TransactionsHistoryDecorator
import com.cryptoexchange.mobile.presentation.more.transactionhistory.filter.TransactionFilterSettings
import com.cryptoexchange.mobile.presentation.more.transactionhistory.filter.TransactionHistoryFilterFragment.Companion.TRANSACTION_FILTER_KEY
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TransactionHistoryFragment : BaseFragment(), TransactionHistoryView {

    private lateinit var binding: FragmentTransactionHistoryBinding
    private lateinit var transactionsAdapter: TransactionHistoryAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: TransactionHistoryPresenter

    @ProvidePresenter
    fun providePresenter(): TransactionHistoryPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        super.setupUI()
        observeBackStackSaveState()
        transactionsAdapter = TransactionHistoryAdapter(presenter::onWithdrawConfirmationNeeded)

        with(binding) {
            filterButton.setOnClickListener { presenter.onFilterClicked() }
            buttonBack.setOnClickListener { presenter.onBackPressed() }

            transactionsRecycler.also {
                val layoutManager = LinearLayoutManager(requireContext())
                val scrollListener = TransactionsAdapterScrollListener(layoutManager) {
                    presenter.onNewPageNeed()
                }
                it.addOnScrollListener(scrollListener)
                it.layoutManager = layoutManager
                it.adapter = transactionsAdapter
                it.addItemDecoration(TransactionsHistoryDecorator())
            }
        }
    }

    override fun showTransactions(transactions: List<MoneyTransactionModel>) {
        transactionsAdapter.setTransactions(transactions)

        with(binding) {
            emptyListImage.visibleUnless(transactions.isNotEmpty())
            emptyListMessage.visibleUnless(transactions.isNotEmpty())
        }
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun showFilter(actionId: Int, bundle: Bundle?) {
        navigate(actionId, bundle)
    }

    override fun updateFilterIcon(drawableRes: Int) {
        binding.filterButton.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                drawableRes
            )
        )
    }

    override fun navigateWithdrawConfirmation(actionId: Int, bundle: Bundle?) {
        navigate(actionId, bundle)
    }

    private fun observeBackStackSaveState() {
        observeBackStackSavedState<TransactionFilterSettings>(
            R.id.transactionHistoryFragment,
            TRANSACTION_FILTER_KEY
        ) {
            presenter.applyFilter(it)
        }
    }
}
