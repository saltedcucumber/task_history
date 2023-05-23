package com.cryptoexchange.mobile.presentation.exchange.filters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cryptoexchange.source.entity.currency.CurrencyResponse
import com.cryptoexchange.source.entity.currency.converter.price.CurrencyPriceModel
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentCurrencyChooserBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.tryToGetParcelable
import com.cryptoexchange.mobile.presentation.exchange.filters.data.CurrencyChoosingInfo
import com.cryptoexchange.mobile.presentation.exchange.filters.data.CurrencyDropDown
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class CurrencyChooserFragment : BaseFragment(), CurrencyChooserView {

    private lateinit var binding: FragmentCurrencyChooserBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: CurrencyChooserPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        val info = tryToGetParcelable<CurrencyChoosingInfo>(CURRENCY_KEY)
        presenter.setData(info)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                presenter.onToolbarBackIconClicked()
            }
        }
    }

    override fun setupCurrencyAdapter(
        displayCurrencies: List<CurrencyResponse>,
        from: CurrencyDropDown
    ) {
        with(binding) {
            currenciesRecyclerView.adapter = CurrencyAdapter(displayCurrencies) { currency ->
                presenter.onCurrencyClicked(currency, from)
            }
        }
    }

    override fun moveBackward(
        currencyResponse: CurrencyResponse,
        from: CurrencyDropDown,
        prices: List<CurrencyPriceModel>
    ) {
        findNavController().apply {
            previousBackStackEntry?.savedStateHandle?.apply {
                set(FROM_KEY, from)
                set(SELECTED_CURRENCY_KEY, currencyResponse)
                set(PRICES_KEY, prices)
            }
            popBackStack()
        }
    }

    override fun navigateBack() {
        popBackStack()
    }

    companion object {

        private const val CURRENCY_KEY = "CURRENCY_KEY"
        const val SELECTED_CURRENCY_KEY = "SELECTED_CURRENCY"
        const val FROM_KEY = "FROM_KEY"
        const val PRICES_KEY = "PRICES_KEY"

        fun getBundle(
            info: CurrencyChoosingInfo,
        ): Bundle {
            return Bundle().apply {
                putParcelable(CURRENCY_KEY, info)
            }
        }
    }
}
