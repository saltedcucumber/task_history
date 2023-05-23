package com.cryptoexchange.mobile.presentation.exchange.filters

import com.cryptoexchange.source.entity.currency.CurrencyResponse
import com.cryptoexchange.source.entity.currency.CurrencyType
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.ExchangeInteractor
import com.cryptoexchange.mobile.presentation.exchange.filters.data.CurrencyChoosingInfo
import com.cryptoexchange.mobile.presentation.exchange.filters.data.CurrencyDropDown
import javax.inject.Inject

class CurrencyChooserPresenter @Inject constructor(
    private val exchangeInteractor: ExchangeInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<CurrencyChooserView>() {

    private var info: CurrencyChoosingInfo? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setupCurrencies()
    }

    // TODO currency type equals null now (need to fix on backend side)
    private fun setupCurrencies() {
        info?.let { info ->
            val displayCurrencies = info.what.filter {
                it.currencyType == CurrencyType.CRYPTO_COIN || it.currencyType == null
            }
            viewState.setupCurrencyAdapter(displayCurrencies, info.from)
        }
    }

    fun setData(info: CurrencyChoosingInfo) {
        this.info = info
    }

    fun onToolbarBackIconClicked() {
        viewState.navigateBack()
    }

    fun onCurrencyClicked(currencyResponse: CurrencyResponse, from: CurrencyDropDown) {
        getPrices(currencyResponse, from)
    }

    private fun getPrices(currencyResponse: CurrencyResponse, from: CurrencyDropDown) {
        exchangeInteractor
            .prices(currencyResponse.shortName)
            .subscribe(
                { viewState.moveBackward(currencyResponse, from, it) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }
}
