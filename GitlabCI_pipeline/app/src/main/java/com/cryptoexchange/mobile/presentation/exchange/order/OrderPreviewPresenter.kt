package com.cryptoexchange.mobile.presentation.exchange.order

import android.util.Log
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.ExchangeInteractor
import javax.inject.Inject

class OrderPreviewPresenter @Inject constructor(
    private val exchangeInteractor: ExchangeInteractor,
    private val errorHandler: ErrorHandler,
) : BasePresenter<OrderPreviewView>() {

    fun onConfirmClicked(info: OrderPreviewInfo) {
        openOrder(info)
    }

    private fun openOrder(info: OrderPreviewInfo) {
        exchangeInteractor.openOrder(
            amount = info.amount,
            marketId = info.marketId,
            side = info.orderSide,
            type = info.orderType,
            price = info.price
        )
            .subscribe(
                { viewState.navigateToSuccessfulConversionScreen() },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Is not possible to open order right now: + $it")
                }
            )
            .connect()
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }

    companion object {

        private val TAG = OrderPreviewPresenter::class.java.simpleName
    }
}
