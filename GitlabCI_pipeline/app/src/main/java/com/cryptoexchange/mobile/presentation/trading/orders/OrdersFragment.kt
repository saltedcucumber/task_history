package com.cryptoexchange.mobile.presentation.trading.orders

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseBindingFragment
import com.cryptoexchange.mobile.core.base.FragmentViewBindingInflateProvider
import com.cryptoexchange.mobile.databinding.FragmentTradingOrdersBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.presentation.trading.orders.adapter.OrdersAdapter
import com.cryptoexchange.source.entity.trade.orders.MarketOrderbookModel
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

typealias Binding = FragmentTradingOrdersBinding

class OrdersFragment : BaseBindingFragment<Binding>(), OrdersView {

    private lateinit var ordersAdapter: OrdersAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: OrdersPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun getBinding(): FragmentViewBindingInflateProvider<Binding> =
        FragmentTradingOrdersBinding::inflate

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun setupUI() {
        with(binding) {
            ordersRecyclerView.apply {
                ordersAdapter = OrdersAdapter()
                adapter = ordersAdapter
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

    override fun onResume() {
        super.onResume()

        presenter.onPause(false)
    }

    override fun onPause() {
        super.onPause()

        presenter.onPause(true)
    }

    override fun showOrders(data: MarketOrderbookModel) {
        ordersAdapter.setOrders(data)
    }

    override fun clearOrders() {
        ordersAdapter.clear()
    }

    override fun updateBuyVolume(text: String) {
        binding.buyVolumeAmountTextView.text = text
    }

    override fun updateSellVolume(text: String) {
        binding.sellVolumeAmountTextView.text = text
    }
}
