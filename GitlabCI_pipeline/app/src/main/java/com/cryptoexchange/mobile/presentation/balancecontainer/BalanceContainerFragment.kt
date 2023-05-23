package com.cryptoexchange.mobile.presentation.balancecontainer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentBalanceContainerBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.google.android.material.tabs.TabLayoutMediator
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class BalanceContainerFragment : BaseFragment(), BalanceContainerView {

    private lateinit var binding: FragmentBalanceContainerBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: BalanceContainerPresenter

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
        binding = FragmentBalanceContainerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val adapter = BalancePagerAdapter(this@BalanceContainerFragment)
            balancePager.adapter = adapter

            TabLayoutMediator(balanceTabs, balancePager) { tab, position ->
                when (position) {
                    0 -> tab.setText(R.string.balance_bottom_tab)
                    1 -> tab.setText(R.string.portfolio)
                }
            }.attach()
        }
    }
}
