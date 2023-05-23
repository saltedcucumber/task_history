package com.cryptoexchange.mobile.presentation.balancecontainer.portfolio

import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.WalletInteractor
import java.math.BigDecimal
import javax.inject.Inject

class PortfolioPresenter @Inject constructor(
    private val walletInteractor: WalletInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<PortfolioView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        walletInteractor
            .getCurrenciesPortfolio()
            .subscribe(
                { currencies ->
                    var total = BigDecimal("0")
                    currencies.forEach { total = total.add(it.available) }
                    viewState.showPortfolio(currencies, total)
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }
}
