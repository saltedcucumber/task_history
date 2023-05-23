package com.cryptoexchange.mobile.presentation.exchange

import android.util.Log
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.core.global.schedulers.SchedulersProvider
import com.cryptoexchange.mobile.domain.interactor.ExchangeInteractor
import com.cryptoexchange.mobile.domain.interactor.TradeInteractor
import com.cryptoexchange.mobile.extensions.*
import com.cryptoexchange.mobile.presentation.exchange.exceptions.MarketIdToFeeIdException
import com.cryptoexchange.mobile.presentation.exchange.filters.data.CurrencyChoosingInfo
import com.cryptoexchange.mobile.presentation.exchange.filters.data.CurrencyDropDown
import com.cryptoexchange.mobile.presentation.exchange.order.OrderPreviewInfo
import com.cryptoexchange.source.entity.currency.CurrencyResponse
import com.cryptoexchange.source.entity.currency.converter.price.CurrencyPriceModel
import com.cryptoexchange.source.entity.market.MarketIdToFeeId
import com.cryptoexchange.source.entity.market.MarketModel
import com.cryptoexchange.source.entity.trade.FeeSetAccountValueResponse
import com.cryptoexchange.source.extensions.getCurrencyIcon
import com.google.android.material.tabs.TabLayout
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import javax.inject.Inject

typealias Amount = BigDecimal
typealias CurrencyName = String
typealias FromPair = Pair<Amount, CurrencyName>
typealias IntoPair = Pair<Amount, CurrencyName>
typealias ConvertPair = Pair<FromPair, IntoPair>

class ExchangePresenter @Inject constructor(
    private val resourceManager: ResourceManager,
    private val exchangeInteractor: ExchangeInteractor,
    private val tradeInteractor: TradeInteractor,
    private val errorHandler: ErrorHandler,
    private val schedulers: SchedulersProvider
) : BasePresenter<ExchangeView>() {

    private var transactionDisposable: Disposable? = null
    private var chosenCurrencyDisposable: Disposable? = null

    private val transactionTypeObservable = BehaviorSubject.createDefault(TransactionType.MARKET)
    private val exchangeTypeObservable = BehaviorSubject.createDefault(ExchangeType.SELL)

    private val givenAmountObservable = BehaviorSubject.createDefault(BigDecimal.ZERO)
    private val receivedAmountObservable = BehaviorSubject.createDefault(BigDecimal.ZERO)
    private val priceInputObservable = BehaviorSubject.createDefault(BigDecimal.ZERO)

    private val givenCurrencyObservable = BehaviorSubject.create<String>()
    private val receivedCurrencyObservable = BehaviorSubject.create<String>()

    private val currencies = mutableListOf<CurrencyResponse>()
    private var marketList = mutableListOf<MarketModel>()
    private var tradeFee = mutableListOf<FeeSetAccountValueResponse>()
    private var receivedPrices = mutableListOf<CurrencyPriceModel>()
    private var givenPrices = mutableListOf<CurrencyPriceModel>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getCurrencyInfo()
        getMarketList()
        getCommission()
        setupExchangeTypeTab()
        observeTransaction()
        observeGivenAmountInput()
        observeReceivedAmountInput()
        observeOrderButtonEnabling()
        observeGivenCurrency()
        observeSwitchingTransaction()
        observeCurrencies()
        observeLimitCourse()
        observeReceivedCurrency()
    }

    private fun observeSwitchingTransaction() {
        exchangeTypeObservable.withLatestFrom(
            givenCurrencyObservable,
            receivedCurrencyObservable,
        ) { _, givenCurrency, receivedCurrency ->
            Pair(givenCurrency, receivedCurrency)
        }
            .distinctUntilChanged()
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.computation())
            .subscribe(
                { (given, received) ->
                    receivedCurrencyObservable.onNext(given)
                    givenCurrencyObservable.onNext(received)
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Error during validation fields observing: + $it")
                }
            )
            .connect()
    }

    private fun observeGivenCurrency() {
        givenCurrencyObservable
            .observeOn(schedulers.ui())
            .subscribe(
                { name ->
                    val icon = resourceManager.getDrawable(getCurrencyIcon(name))
                    viewState.setGivenCurrency(name, icon)
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Error during validation fields observing: + $it")
                }
            )
            .connect()
    }

    private fun observeReceivedCurrency() {
        receivedCurrencyObservable
            .observeOn(schedulers.ui())
            .subscribe(
                { name ->
                    val icon = resourceManager.getDrawable(getCurrencyIcon(name))
                    viewState.setReceivedCurrency(name, icon)
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Error during validation fields observing: + $it")
                }
            )
            .connect()
    }

    private fun observeLimitCourse() {
        Observable.combineLatest(
            transactionTypeObservable,
            givenAmountObservable,
            receivedAmountObservable,
            givenCurrencyObservable,
            receivedCurrencyObservable,
            priceInputObservable
        ) { transactionType, givenAmount, receivedAmount, givenCurrency, receivedCurrency, price ->
            Pair(
                Triple(transactionType, givenAmount, receivedAmount),
                Triple(givenCurrency, receivedCurrency, price)
            ) to Pair(
                Triple(transactionType, givenAmount, receivedAmount),
                Triple(givenCurrency, receivedCurrency, price)
            )
        }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.computation())
            .map {
                val (transactionType, givenAmount, receivedAmount) = it.first.first
                val (givenCurrency, receivedCurrency, price) = it.first.second

                var course = getCourse(receivedCurrency)
                val converted = when (transactionType) {
                    TransactionType.LIMIT -> {
                        givenAmount.multiplyWithPrecision(
                            if (price.identical(BigDecimal.ZERO)) {
                                course
                            } else {
                                course = price
                                price
                            },
                            MathContext.DECIMAL32
                        )
                    }
                    TransactionType.MARKET -> {
                        givenAmount.multiplyWithPrecision(course, MathContext.DECIMAL32)
                    }
                }
                    .stripTrailingZeros()

                val isVisible = givenAmount.notIdentical(BigDecimal.ZERO) &&
                    receivedAmount.notIdentical(BigDecimal.ZERO) &&
                    !givenCurrency.isNullOrBlank() &&
                    !receivedCurrency.isNullOrBlank()

                val formattedCurrency =
                    "${BigDecimal.ONE} $givenCurrency = " +
                        "${course.toDisplayStringBy()} $receivedCurrency"

                var commission = BigDecimal.ZERO
                val exType = exchangeTypeObservable.value
                if (exType != null) {
                    val fee = defineCommission(exType).stripTrailingZeros()
                    commission = when (exType) {
                        ExchangeType.BUY -> converted.calculateCommission(fee)
                            .setScale(CURRENCY_LENGTH, RoundingMode.HALF_UP)
                        else -> converted.calculateCommission(fee)
                            .setScale(CURRENCY_LENGTH, RoundingMode.HALF_UP)
                    }
                }
                val feePattern = resourceManager.getString(R.string.exchange_fee)
                val formattedCommission =
                    String.format(feePattern, commission.toDisplayStringBy(), receivedCurrency)

                Pair((isVisible to formattedCurrency), (isVisible to formattedCommission))
            }
            .observeOn(schedulers.ui())
            .subscribe(
                { (currencyTip, commissionTip) ->
                    viewState.updateConvertingTip(currencyTip.first, currencyTip.second)
                    viewState.updateCommissionTip(commissionTip.first, commissionTip.second)
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "observeLimitCourse")
                }
            )
            .connect()
    }

    private fun observeGivenAmountInput() {
        givenAmountObservable.withLatestFrom(
            givenCurrencyObservable,
            receivedCurrencyObservable
        ) { amount, shortName, receivedCurrency ->
            Pair(FromPair(amount, shortName), receivedCurrency as CurrencyName)
        }
            .distinctUntilChanged()
            .filter { isCurrenciesChosen() }
            .map { (from, receivedCurrency) ->
                val course = getCourse(receivedCurrency)
                from.first.multiplyWithPrecision(course)
                    .setScale(CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
            }
            .withLatestFrom(receivedAmountObservable) { converted, received ->
                converted to received
            }
            .filter { (converted, received) -> converted.notIdentical(received) }
            .map { (converted, _) -> converted.stripTrailingZeros() }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
            .subscribe(
                { converted ->
                    receivedAmountObservable.onNext(converted)
                    viewState.updateReceivedAmount(
                        converted.toPlainStringExceptZero()
                    )
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Error during validation fields observing: + $it")
                }
            )
            .connect()
    }

    private fun observeReceivedAmountInput() {
        receivedAmountObservable.withLatestFrom(
            receivedCurrencyObservable,
            givenCurrencyObservable
        ) { amount, shortName, givenCurrency ->
            Pair(IntoPair(amount, shortName), givenCurrency as CurrencyName)
        }
            .distinctUntilChanged()
            .filter { isCurrenciesChosen() }
            .map { (into, _) ->
                val (amount, currency) = into
                val course = getCourse(currency)
                amount.divide(course, MathContext.DECIMAL128)
                    .setScale(CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
            }
            .withLatestFrom(givenAmountObservable) { converted, givenAmount ->
                converted to givenAmount
            }
            .filter { (converted, given) -> converted.notIdentical(given) }
            .map { (converted, _) -> converted.stripTrailingZeros() }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
            .subscribe(
                { converted ->
                    givenAmountObservable.onNext(converted)
                    viewState.updateGivenAmount(
                        converted.toPlainStringExceptZero()
                    )
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Error during validation fields observing: + $it")
                }
            )
            .connect()
    }

    private fun observeCurrencies() {
        Observable.combineLatest(
            givenCurrencyObservable,
            receivedCurrencyObservable
        ) { givenCurrency, receivedCurrency ->
            Pair(givenCurrency, receivedCurrency)
        }
            .withLatestFrom(
                givenAmountObservable,
                receivedAmountObservable
            ) { currenciesPair, givenAmount, receivedAmount ->
                val (givenCurrency, receivedCurrency) = currenciesPair
                ConvertPair(
                    FromPair(givenAmount, givenCurrency),
                    IntoPair(receivedAmount, receivedCurrency)
                )
            }
            .distinctUntilChanged()
            .map { (from, into) ->
                val course = getCourse(into.second)
                val converted = from.first.multiplyWithPrecision(course)
                converted to into.first
            }
            .filter { (converted, received) ->
                converted.notIdentical(received)
            }
            .map { (converted, _) -> converted }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
            .subscribe(
                { converted ->
                    viewState.updateReceivedAmount(
                        converted.setScale(CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
                            .toPlainStringExceptZero()
                    )
                },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Error during validation fields observing: + $it")
                }
            )
            .connect()
    }

    private fun observeOrderButtonEnabling() {
        Observable.combineLatest(
            transactionTypeObservable,
            givenAmountObservable,
            receivedAmountObservable,
            priceInputObservable,
            givenCurrencyObservable,
            receivedCurrencyObservable
        ) { type, buy, sell, price, paymentType, consumerType ->
            paymentType.isNotEmpty() && consumerType.isNotEmpty() && ExchangeValidationFields(
                buy,
                sell,
                price
            ).isValid(type)
        }
            .distinctUntilChanged()
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
            .subscribe(
                { isValid -> viewState.transactionButtonEnabled(isValid) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Error during validation fields observing: + $it")
                }
            )
            .connect()
    }

    private fun getCommission() {
        tradeInteractor
            .getCommissions()
            .observeOn(schedulers.computation())
            .subscribe(
                { tradeFee.clearAndAddAll(it) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Some problems when trying to request commission: + $it")
                }
            )
            .connect()
    }

    private fun getCurrencyInfo() {
        exchangeInteractor
            .loadCurrencies()
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.computation())
            .subscribe(
                { currencies.clearAndAddAll(it) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Currency is not available: + $it")
                }
            )
            .connect()
    }

    private fun setupExchangeTypeTab() {
        val labels = resourceManager.run {
            arrayOf(TransactionType.MARKET, TransactionType.LIMIT)
        }
        viewState.setupOrderTypeTab(labels, TabLayout.GRAVITY_FILL)
    }

    fun switchTransaction(transactionType: TransactionType) {
        transactionTypeObservable.onNext(transactionType)
    }

    private fun observeTransaction() {
        transactionTypeObservable
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
            .subscribe(
                { type -> onTransactionTypeChanged(type) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Error during transaction type observing")
                }
            )
            .connect()
    }

    private fun onTransactionTypeChanged(transactionType: TransactionType) {
        with(viewState) {
            switchTransactionType(transactionType)
            changePriceFieldVisibility(transactionType == TransactionType.LIMIT)
        }
    }

    fun onReverseButtonClicked() {
        val exType = exchangeTypeObservable.value
        if (exType != null) {
            exchangeTypeObservable.onNext(exType.nextValue)
        }
    }

    fun onCurrencyChosen(
        currency: CurrencyResponse,
        from: CurrencyDropDown,
        prices: List<CurrencyPriceModel>
    ) {
        when (exchangeTypeObservable.value) {
            ExchangeType.SELL -> when (from) {
                CurrencyDropDown.GIVE -> {
                    givenPrices.clearAndAddAll(prices)
                }
                CurrencyDropDown.RECEIVE -> {
                    receivedPrices.clearAndAddAll(prices)
                }
            }

            ExchangeType.BUY -> when (from) {
                CurrencyDropDown.GIVE -> {
                    receivedPrices.clearAndAddAll(prices)
                }
                CurrencyDropDown.RECEIVE -> {
                    givenPrices.clearAndAddAll(prices)
                }
            }
            else -> when (from) {
                CurrencyDropDown.GIVE -> {
                    receivedPrices.clearAndAddAll(prices)
                }
                CurrencyDropDown.RECEIVE -> {
                    givenPrices.clearAndAddAll(prices)
                }
            }
        }

        when (from) {
            CurrencyDropDown.GIVE -> {
                givenCurrencyObservable.onNext(currency.shortName)
            }
            CurrencyDropDown.RECEIVE -> {
                receivedCurrencyObservable.onNext(currency.shortName)
            }
        }
    }

    fun onGivenInputChanged(text: String) {
        val value = text.toBigDecimalOrNull() ?: BigDecimal.ZERO

        if (value.smallerOrIdentical(BigDecimal.ZERO)) {
            givenAmountObservable.onNext(BigDecimal.ZERO)
            return
        }
        givenAmountObservable.onNext(value)
    }

    fun onReceivedInputChanged(text: String) {
        val value = text.toBigDecimalOrNull() ?: BigDecimal.ZERO

        if (value.smallerOrIdentical(BigDecimal.ZERO)) {
            receivedAmountObservable.onNext(BigDecimal.ZERO)
            return
        }
        receivedAmountObservable.onNext(value)
    }

    fun onPriceInputChanged(text: CharSequence?) {
        priceInputObservable.onNext(text.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO)
    }

    fun onGivenCurrencyClicked() {
        val availableCurrency = when (exchangeTypeObservable.value) {
            ExchangeType.SELL -> {
                marketList.map { it.leftCurrencyCode }
            }
            ExchangeType.BUY -> {
                (
                    marketList
                        .filter { it.leftCurrencyCode == receivedCurrencyObservable.value }
                        .takeIf { it.isNotEmpty() } ?: marketList
                    )
                    .map { it.rightCurrencyCode }
            }
            else -> {
                marketList.map { it.leftCurrencyCode }
            }
        }

        val displayCurrencies = currencies.filter { availableCurrency.contains(it.shortName) }
        val currencyData = CurrencyChoosingInfo(displayCurrencies, CurrencyDropDown.GIVE)
        viewState.navigateToCurrencyChoosing(currencyData)
    }

    fun onReceivedCurrencyClicked() {
        val availableCurrency = when (exchangeTypeObservable.value) {
            ExchangeType.SELL -> {
                (
                    marketList
                        .filter { it.leftCurrencyCode == givenCurrencyObservable.value }
                        .takeIf { it.isNotEmpty() } ?: marketList
                    )
                    .map { it.rightCurrencyCode }
            }
            ExchangeType.BUY -> {
                marketList.map { it.leftCurrencyCode }
            }
            else -> {
                marketList.map { it.rightCurrencyCode }
            }
        }

        val displayCurrencies =
            currencies.filter { availableCurrency.contains(it.shortName) }
        val info = CurrencyChoosingInfo(displayCurrencies, CurrencyDropDown.RECEIVE)
        viewState.navigateToCurrencyChoosing(info)
    }

    fun onOrderButtonClicked() {
        val isGivenAmountExceeded =
            givenAmountObservable.value >= BigDecimal(CURRENCY_GIVEN_INPUT_MAX_VALUE)
        if (!isGivenAmountExceeded) {
            createOrder()
        }
        val givenAmountExceeded =
            resourceManager.getErrorMessage(!isGivenAmountExceeded, R.string.amount_exceeded)
        viewState.showTooBigAmountError(givenAmountExceeded)
    }

    private fun createOrder() {
        transactionDisposable = Observable.combineLatest(
            transactionTypeObservable,
            exchangeTypeObservable,
            givenAmountObservable,
            receivedAmountObservable,
            priceInputObservable,
            givenCurrencyObservable,
            receivedCurrencyObservable
        ) { type, exType, givenAmount, receivedAmount, price, paymentType, consumerType ->
            val (marketId) = defineMarketIdAndFeeId(paymentType, consumerType)
            val commission = defineCommission(exType)

            OrderPreviewInfo(
                amount = givenAmount,
                paymentType = paymentType,
                orderType = type.convertedBackendValue,
                orderSide = exType.convertedBackendValue,
                marketId = marketId,
                price = price.stripTrailingZeros(),
                total = receivedAmount,
                consumerType = consumerType,
                fee = commission.stripTrailingZeros(),
                course = getCourse(consumerType),
                transactionType = type
            )
        }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
            .subscribe(
                { info -> viewState.navigateToConfirmation(info) },
                {
                    val errorMessage = when (it) {
                        is MarketIdToFeeIdException -> {
                            R.string.market_id_to_fee_id_exception_message
                        }
                        else -> {
                            R.string.something_goes_wrong
                        }
                    }
                    viewState.showMessage(errorMessage, MessageType.FAILED)
                    Log.e(TAG, "Error during transaction type observing: + $it")
                }
            )
    }

    override fun detachView(view: ExchangeView?) {
        super.detachView(view)
        transactionDisposable?.dispose()
        chosenCurrencyDisposable?.dispose()
        chosenCurrencyDisposable = null
        transactionDisposable = null
    }

    private fun defineMarketIdAndFeeId(
        paymentType: String,
        consumerType: String
    ): MarketIdToFeeId {
        return marketList.find { market ->
            (market.leftCurrencyCode == paymentType && market.rightCurrencyCode == consumerType) ||
                (market.leftCurrencyCode == consumerType && market.rightCurrencyCode == paymentType)
        }?.run {
            id to feeSetId
        } ?: throw MarketIdToFeeIdException()
    }

    private fun defineCommission(exType: ExchangeType): BigDecimal {
        val feeSet = tradeFee.firstOrNull()
        return exType.tradeFee(feeSet)
    }

    private fun getMarketList() {
        exchangeInteractor
            .marketList()
            .subscribeOn(schedulers.computation())
            .subscribe(
                { marketList.clearAndAddAll(it) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                    Log.e(TAG, "Market list is not available: + $it")
                }
            )
            .connect()
    }

    private fun getCourse(
        currencyName: String?
    ): BigDecimal {
        val prices = when (exchangeTypeObservable.value) {
            ExchangeType.SELL -> givenPrices
            ExchangeType.BUY -> receivedPrices
            else -> givenPrices
        }
        return prices.find {
            it.shortName.equals(currencyName, true)
        }?.price ?: BigDecimal.ONE
    }

    private fun isCurrenciesChosen() = givenCurrencyObservable.hasValue() &&
        receivedCurrencyObservable.hasValue()

    companion object {

        private val TAG = ExchangePresenter::class.java.simpleName

        private const val CURRENCY_GIVEN_INPUT_MAX_VALUE = 100000
    }
}
