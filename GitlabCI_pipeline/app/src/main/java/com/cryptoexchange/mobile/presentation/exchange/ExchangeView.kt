package com.cryptoexchange.mobile.presentation.exchange

import android.graphics.drawable.Drawable
import com.cryptoexchange.mobile.core.base.BaseView
import com.cryptoexchange.mobile.presentation.exchange.filters.data.CurrencyChoosingInfo
import com.cryptoexchange.mobile.presentation.exchange.order.OrderPreviewInfo
import com.google.android.material.tabs.TabLayout.TabGravity
import moxy.viewstate.strategy.*

interface ExchangeView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupOrderTypeTab(types: Array<TransactionType>, @TabGravity tabGravity: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun switchTransactionType(transactionType: TransactionType)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun changePriceFieldVisibility(isLimit: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToConfirmation(info: OrderPreviewInfo)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun transactionButtonEnabled(isEnabled: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateGivenAmount(convertedValue: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateReceivedAmount(convertedValue: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToCurrencyChoosing(info: CurrencyChoosingInfo)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setGivenCurrency(currencyName: String, icon: Drawable?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setReceivedCurrency(currencyName: String, icon: Drawable?)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateConvertingTip(isVisible: Boolean, text: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateCommissionTip(isVisible: Boolean, text: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showTooBigAmountError(message: String)
}
