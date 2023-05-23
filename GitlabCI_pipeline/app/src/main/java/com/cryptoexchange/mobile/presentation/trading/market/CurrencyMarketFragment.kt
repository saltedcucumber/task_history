package com.cryptoexchange.mobile.presentation.trading.market

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseBindingFragment
import com.cryptoexchange.mobile.core.base.FragmentViewBindingInflateProvider
import com.cryptoexchange.mobile.databinding.FragmentCurrencyMarketBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.tryToGetParcelable
import com.cryptoexchange.mobile.presentation.trading.market.adapter.CurrencyMarketAdapter
import com.cryptoexchange.source.entity.market.MarketModel
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class CurrencyMarketFragment :
    BaseBindingFragment<FragmentCurrencyMarketBinding>(),
    CurrencyMarketView {

    @Inject
    @InjectPresenter
    lateinit var presenter: CurrencyMarketPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun getBinding(): FragmentViewBindingInflateProvider<FragmentCurrencyMarketBinding> =
        FragmentCurrencyMarketBinding::inflate

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)

        val marketModel = tryToGetParcelable<MarketModel>(KEY_CURRENT_MARKET_MODEL)
        presenter.data(marketModel)
    }

    override fun setupUI() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                popBackStack()
            }
            applyButton.setOnClickListener {
                presenter.onApplyClicked()
            }
            currenciesRecyclerView.apply {
                itemAnimator = null
                layoutManager = LinearLayoutManager(requireContext()).also {
                    addItemDecoration(
                        DividerItemDecoration(requireContext(), it.orientation).apply {
                            val height = resources.getDimensionPixelOffset(
                                R.dimen.currency_market_item_margin
                            )
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

    override fun setupCurrencyAdapter(markets: List<MarketModel>, currencyPair: Int) {
        with(binding.currenciesRecyclerView) {
            adapter = CurrencyMarketAdapter(markets, currencyPair) { market, position ->
                post {
                    adapter?.notifyItemChanged(position)
                }
                presenter.onCurrencyPairChanged(market)
            }
        }
    }

    override fun navigateBack() {
        popBackStack()
    }

    companion object {
        private const val KEY_CURRENT_MARKET_MODEL = "KEY_CURRENT_MARKET_MODEL"

        fun getBundle(currentMarketModel: MarketModel): Bundle =
            Bundle().apply {
                putParcelable(KEY_CURRENT_MARKET_MODEL, currentMarketModel)
            }
    }
}
