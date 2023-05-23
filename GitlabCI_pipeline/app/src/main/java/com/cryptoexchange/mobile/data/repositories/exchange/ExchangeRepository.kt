package com.cryptoexchange.mobile.data.repositories.exchange

import com.cryptoexchange.mobile.domain.entity.CurrencyHistory
import com.cryptoexchange.source.entity.currency.CurrencyResponse
import com.cryptoexchange.source.entity.currency.converter.price.CurrencyPriceModel
import com.cryptoexchange.source.entity.market.MarketModel
import com.cryptoexchange.source.entity.order.OrderSide
import com.cryptoexchange.source.entity.order.OrderType
import com.cryptoexchange.source.entity.order.OrderModel
import com.cryptoexchange.mobile.extensions.clearAndAddAll
import com.cryptoexchange.source.entity.currency.converter.history.ExchangeHistoryModel
import com.cryptoexchange.source.entity.order.OrderWrapperModel
import com.cryptoexchange.source.entrypoint.managers.*
import com.cryptoexchange.source.extensions.getBaseCurrencyToConvert
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.math.BigDecimal

class ExchangeRepository(
    private val marketManager: MarketManager,
    private val orderManager: OrderManager,
    private val currencyConverterManager: CurrencyConverterManager,
    private val currencyManager: CurrencyManager
) {
    private val _cachedCurrencies: MutableList<CurrencyResponse> = mutableListOf()
    val cachedCurrencies: List<CurrencyResponse> = _cachedCurrencies

    fun loadAndCacheCurrencies(): Single<List<CurrencyResponse>> =
        Single.fromCallable {
            currencyManager.getCurrenciesList()
        }.doOnSuccess {
            _cachedCurrencies.clearAndAddAll(it)
        }

    fun getRates(baseCurrency: String): Single<List<CurrencyPriceModel>> {
        return Single.fromCallable { currencyConverterManager.getRates(baseCurrency) }
    }

    fun openOrder(
        amount: BigDecimal,
        marketId: Long,
        side: OrderSide,
        type: OrderType,
        price: BigDecimal?
    ): Single<OrderModel> {
        return Single.fromCallable {
            orderManager.openOrder(
                amount,
                marketId,
                side,
                type,
                price
            )
        }
    }

    fun getMarketsList(): Single<List<MarketModel>> {
        return Single.fromCallable {
            marketManager.getMarketsList()
        }
    }

    fun getExchangeHistory(dateFrom: String, dateTo: String): Single<List<CurrencyHistory>> =
        Single.fromCallable {
            currencyManager.getCurrenciesList()
        }
            .toObservable()
            .flatMapIterable { currencyModel ->
                currencyModel.map { it.shortName }
            }
            .flatMapSingle { currencyName ->
                Single.fromCallable {
                    currencyConverterManager.getExchangeHistory(
                        currencyName.lowercase(),
                        getBaseCurrencyToConvert().lowercase(),
                        dateFrom,
                        dateTo
                    ) ?: ExchangeHistoryModel(emptyList())
                }
                    .map { CurrencyHistory(currencyName, it.exchangeRates) }
                    .subscribeOn(Schedulers.computation())
            }
            .toList()
            .map {
                it.filter { history -> history.currencyRates.isNotEmpty() }
            }

    fun getOrdersList(): Single<OrderWrapperModel> =
        Single.fromCallable {
            orderManager.getOrdersList()
        }

    fun closeOrder(orderId: Long): Completable =
        Completable.fromCallable {
            orderManager.closeOrder(orderId)
        }
}