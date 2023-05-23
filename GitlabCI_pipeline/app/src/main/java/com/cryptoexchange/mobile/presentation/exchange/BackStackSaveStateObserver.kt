package com.cryptoexchange.mobile.presentation.exchange

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.cryptoexchange.source.entity.currency.CurrencyResponse
import com.cryptoexchange.source.entity.currency.converter.price.CurrencyPriceModel
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.presentation.exchange.filters.CurrencyChooserFragment.Companion.FROM_KEY
import com.cryptoexchange.mobile.presentation.exchange.filters.CurrencyChooserFragment.Companion.PRICES_KEY
import com.cryptoexchange.mobile.presentation.exchange.filters.CurrencyChooserFragment.Companion.SELECTED_CURRENCY_KEY
import com.cryptoexchange.mobile.presentation.exchange.filters.data.CurrencyDropDown

class BackStackSaveStateObserver(
    private val exchangeFragment: ExchangeFragment,
    private val onSaveStateExist: (
        currency: CurrencyResponse,
        from: CurrencyDropDown,
        prices: List<CurrencyPriceModel>
    ) -> Unit
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        observeBackStackSaveState()
    }

    private fun observeBackStackSaveState() {
        val navController = exchangeFragment.findNavController()
        val navBackStackEntry = navController.getBackStackEntry(R.id.exchangeDestination)

        val observer = LifecycleEventObserver { _, event ->
            with(navBackStackEntry.savedStateHandle) {
                if (event == Lifecycle.Event.ON_RESUME &&
                    contains(SELECTED_CURRENCY_KEY) &&
                    contains(FROM_KEY)
                ) {
                    val currency = get<CurrencyResponse>(SELECTED_CURRENCY_KEY)
                    val from = get<CurrencyDropDown>(FROM_KEY)
                    val prices = get<List<CurrencyPriceModel>>(PRICES_KEY)

                    if (currency != null && from != null && prices != null) {
                        onSaveStateExist.invoke(currency, from, prices)
                        remove<CurrencyResponse>(SELECTED_CURRENCY_KEY)
                        remove<CurrencyDropDown>(FROM_KEY)
                    }
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        exchangeFragment.viewLifecycleOwner.lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            }
        )
    }
}