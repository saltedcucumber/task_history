package com.cryptoexchange.mobile.presentation.balancecontainer.portfolio

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentPortfolioBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.domain.entity.CurrencyInBasic
import com.cryptoexchange.mobile.extensions.visibleUnless
import com.cryptoexchange.mobile.presentation.balancecontainer.portfolio.adapter.PortfolioAdapter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.math.BigDecimal
import javax.inject.Inject

class PortfolioFragment : BaseFragment(), PortfolioView {

    private lateinit var binding: FragmentPortfolioBinding
    private lateinit var adapter: PortfolioAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: PortfolioPresenter

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
        binding = FragmentPortfolioBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun showPortfolio(currenciesInBasic: List<CurrencyInBasic>, totalBalance: BigDecimal) {
        with(binding) {
            portfolioAdapter.layoutManager = LinearLayoutManager(requireContext())
            adapter = PortfolioAdapter(currenciesInBasic, totalBalance)
            portfolioAdapter.adapter = adapter

            emptyPortfolioIcon.visibleUnless(currenciesInBasic.isNotEmpty())
            emptyPortfolioText.visibleUnless(currenciesInBasic.isNotEmpty())
        }
    }
}
