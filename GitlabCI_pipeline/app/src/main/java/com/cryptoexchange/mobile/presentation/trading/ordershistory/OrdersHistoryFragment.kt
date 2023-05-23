package com.cryptoexchange.mobile.presentation.trading.ordershistory

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseBindingFragment
import com.cryptoexchange.mobile.core.base.FragmentViewBindingInflateProvider
import com.cryptoexchange.mobile.databinding.FragmentTradingOrdersHistoryBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.presentation.trading.ordershistory.adapter.OrderHistoryAdapter
import com.cryptoexchange.source.entity.trade.history.recent.TradeRecentOrderHistoryModel
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

typealias Binding = FragmentTradingOrdersHistoryBinding

class OrdersHistoryFragment : BaseBindingFragment<Binding>(), OrdersHistoryView {

    private var ordersAdapter: OrderHistoryAdapter? = null

    @Inject
    @InjectPresenter
    lateinit var presenter: OrdersHistoryPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun getBinding(): FragmentViewBindingInflateProvider<Binding> =
        FragmentTradingOrdersHistoryBinding::inflate

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    // TODO try shape drawable
    override fun setupUI() {
        with(binding) {
            ordersRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext()).also {
                    addItemDecoration(
                        DividerItemDecoration(requireContext(), it.orientation).apply {
                            val height =
                                resources.getDimensionPixelOffset(R.dimen.order_history_margin)
                            val drawable = GradientDrawable().also { drawable ->
                                drawable.shape = GradientDrawable.RECTANGLE
                                drawable.setSize(0, height)
                            }
                            setDrawable(drawable)
                        }
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ordersAdapter = null
    }

    override fun showHistory(data: List<TradeRecentOrderHistoryModel>) {
        with(binding.ordersRecyclerView) {
            ordersAdapter = OrderHistoryAdapter(data).also {
                adapter = it
            }
        }
    }

    override fun updateAmountColumn(text: String) {
        binding.amountHeaderTextView.text = text
    }

    override fun updatePriceColumn(text: String) {
        binding.priceHeaderTextView.text = text
    }
}
