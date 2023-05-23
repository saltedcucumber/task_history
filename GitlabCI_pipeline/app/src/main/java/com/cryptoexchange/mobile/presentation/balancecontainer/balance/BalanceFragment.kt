package com.cryptoexchange.mobile.presentation.balancecontainer.balance

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentBalanceBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.adapter.BalanceAdapter
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.deposit.DepositFragment
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.DepositType
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.WithdrawType
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.WithdrawFragment
import com.cryptoexchange.source.entity.wallet.WalletModel
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class BalanceFragment : BaseFragment(), BalanceView {

    private lateinit var binding: FragmentBalanceBinding
    private lateinit var balanceAdapter: BalanceAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: BalancePresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBalanceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun setupUI() {
        with(binding) {
            balanceAdapter = BalanceAdapter(
                { presenter.onDepositClicked(it) },
                { presenter.onWithdrawClicked(it) }
            )

            balanceRecycler.adapter = balanceAdapter
            balanceRecycler.layoutManager = LinearLayoutManager(requireContext())

            zeroBalancesSwitch.setOnCheckedChangeListener { _, isChecked ->
                presenter.onHideZeroSwitched(isChecked)
            }
            balanceSearch.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String): Boolean = true

                    override fun onQueryTextChange(newText: String): Boolean {
                        presenter.onSearchChanged(newText)

                        return true
                    }
                }
            )
            changeSearchViewTextColor(balanceSearch)
        }
    }

    override fun navigateToDeposit(depositType: DepositType) {
        navigate(
            R.id.action_balanceDestination_to_depositFragment,
            DepositFragment.getBundle(depositType)
        )
    }

    override fun navigateToWithdraw(withdrawType: WithdrawType) {
        navigate(
            R.id.action_balanceDestination_to_withdrawFragment,
            WithdrawFragment.getBundle(withdrawType)
        )
    }

    override fun showCurrencies(currencies: List<WalletModel>) {
        balanceAdapter.setCurrencies(currencies)
    }

    override fun updateUiState(isHideZero: Boolean, searchQuery: String) {
        with(binding) {
            zeroBalancesSwitch.isChecked = isHideZero
            balanceSearch.setQuery(searchQuery, false)
        }
    }

    private fun changeSearchViewTextColor(view: View?) {
        if (view != null) {
            val textColor = ContextCompat.getColor(requireContext(), R.color.tundora)
            if (view is TextView) {
                view.setTextColor(textColor)
                return
            } else if (view is ViewGroup) {
                val viewGroup = view
                for (i in 0 until viewGroup.childCount) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i))
                }
            }
        }
    }
}
