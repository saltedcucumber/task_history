package com.cryptoexchange.mobile.presentation.trading

import android.content.Context
import android.os.Bundle
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseBindingFragment
import com.cryptoexchange.mobile.core.base.FragmentViewBindingInflateProvider
import com.cryptoexchange.mobile.databinding.FragmentTradingBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.goneUnless
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.presentation.trading.adapter.TradingPagerAdapter
import com.google.android.material.tabs.TabLayout
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TradingFragment : BaseBindingFragment<FragmentTradingBinding>(), TradingView {

    @Inject
    @InjectPresenter
    lateinit var presenter: TradingPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun getBinding(): FragmentViewBindingInflateProvider<FragmentTradingBinding> =
        FragmentTradingBinding::inflate

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun setupUI() {
        with(binding) {
            val adapter = TradingPagerAdapter(requireContext(), childFragmentManager)
            tradingPager.adapter = adapter
            topBar.setupWithViewPager(tradingPager)

            currencyPairTextView.setOnClickListener {
                presenter.onChangePairClicked()
            }

            topBar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    presenter.onTabChanged(tab.position)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}

                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }
    }

    override fun updateCurrencyPair(text: String) {
        binding.currencyPairTextView.text = text
    }

    override fun navigateToCurrencyMarketScreen(bundle: Bundle) {
        navigate(R.id.action_tradingDestination_to_currencyMarketFragment, bundle)
    }

    override fun showCurrencyPairButton(isShow: Boolean) {
        binding.currencyPairTextView.goneUnless(isShow)
    }
}
