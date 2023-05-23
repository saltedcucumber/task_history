package com.cryptoexchange.mobile.presentation.trading.myorders

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptoexchange.mobile.core.base.BaseBindingFragment
import com.cryptoexchange.mobile.core.base.FragmentViewBindingInflateProvider
import com.cryptoexchange.mobile.databinding.FragmentMyOrdersBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.visibleUnless
import com.cryptoexchange.mobile.presentation.trading.myorders.adapter.MyOrdersAdapter
import com.cryptoexchange.source.entity.order.OrderModel
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class MyOrdersFragment :
    BaseBindingFragment<FragmentMyOrdersBinding>(),
    MyOrdersView,
    CloseOrderDialogFragment.OnClickListener {

    private lateinit var myOrdersAdapter: MyOrdersAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: MyOrdersPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun getBinding(): FragmentViewBindingInflateProvider<FragmentMyOrdersBinding> =
        FragmentMyOrdersBinding::inflate

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun setupUI() {
        super.setupUI()

        with(binding.myOrdersRecycler) {
            layoutManager = LinearLayoutManager(requireContext())
            myOrdersAdapter = MyOrdersAdapter(presenter::onCloseClicked)
            adapter = myOrdersAdapter
        }
    }

    override fun showOrders(orders: List<Pair<OrderModel, String>>) {
        binding.emptyOrdersIcon.visibleUnless(orders.isNotEmpty())
        binding.emptyOrdersText.visibleUnless(orders.isNotEmpty())
        myOrdersAdapter.setOrders(orders)
    }

    override fun showConfirmationDialog(position: Int) {
        if (childFragmentManager.findFragmentByTag(TAG_CONFIRMATION_DIALOG) == null) {
            val dialog = CloseOrderDialogFragment.getInstance(position)

            dialog.show(childFragmentManager, TAG_CONFIRMATION_DIALOG)
        }
    }

    override fun suggest(position: Int) {
        presenter.onCloseOrderSuggested(position)
    }

    companion object {
        private const val TAG_CONFIRMATION_DIALOG = "TAG_CONFIRMATION_DIALOG"
    }
}
