package com.cryptoexchange.mobile.presentation.more.tradinghistory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentTradingHistoryBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.domain.entity.TradingHistory
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.observeBackStackSavedState
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.visibleUnless
import com.cryptoexchange.mobile.presentation.more.tradinghistory.adapter.TradingAdapterScrollListener
import com.cryptoexchange.mobile.presentation.more.tradinghistory.adapter.TradingHistoryAdapter
import com.cryptoexchange.mobile.presentation.more.tradinghistory.filter.TradingFilterSettings
import com.cryptoexchange.mobile.presentation.more.tradinghistory.filter.TradingHistoryFilterFragment.Companion.TRADING_FILTER_KEY
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TradingHistoryFragment : BaseFragment(), TradingHistoryView {

    private lateinit var binding: FragmentTradingHistoryBinding
    private lateinit var transactionsAdapter: TradingHistoryAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: TradingHistoryPresenter

    @ProvidePresenter
    fun providePresenter(): TradingHistoryPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTradingHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        super.setupUI()
        observeBackStackSaveState()
        transactionsAdapter = TradingHistoryAdapter { presenter.onDetailsClicked(it) }

        with(binding) {
            filterButton.setOnClickListener { presenter.onFilterClicked() }
            buttonBack.setOnClickListener { presenter.onBackPressed() }

            transactionsRecycler.also {
                val layoutManager = LinearLayoutManager(requireContext())
                val scrollListener = TradingAdapterScrollListener(layoutManager) {
                    presenter.onNewPageNeed()
                }
                it.addOnScrollListener(scrollListener)
                it.layoutManager = layoutManager
                it.adapter = transactionsAdapter
            }
        }
    }

    override fun showTradings(tradings: List<TradingHistory>) {
        transactionsAdapter.submitList(tradings)

        with(binding) {
            emptyListImage.visibleUnless(tradings.isNotEmpty())
            emptyListMessage.visibleUnless(tradings.isNotEmpty())
        }
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun showDetails(actionId: Int, bundle: Bundle?) {
        navigate(actionId, bundle)
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

    private fun observeBackStackSaveState() {
        observeBackStackSavedState<TradingFilterSettings>(
            R.id.tradingHistoryFragment,
            TRADING_FILTER_KEY
        ) {
            presenter.applyFilter(it)
        }
    }
}
