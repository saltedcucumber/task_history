package com.cryptoexchange.mobile.domain.interactor

import com.cryptoexchange.source.entity.currency.CurrencyResponse
import com.cryptoexchange.source.entity.currency.converter.price.CurrencyPriceModel
import com.cryptoexchange.source.entity.market.MarketModel
import com.cryptoexchange.source.entity.order.OrderSide
import com.cryptoexchange.source.entity.order.OrderType
import com.cryptoexchange.source.entity.order.OrderModel
import com.cryptoexchange.mobile.core.global.schedulers.SchedulersProvider
import com.cryptoexchange.mobile.data.repositories.exchange.ExchangeRepository
import com.cryptoexchange.mobile.domain.entity.CurrencyHistory
import com.cryptoexchange.source.entity.order.OrderWrapperModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.math.BigDecimal
import javax.inject.Inject

class ExchangeInteractor @Inject constructor(
    private val exchangeRepository: ExchangeRepository,
    private val schedulers: SchedulersProvider
) {
    val cachedCurrencies = exchangeRepository.cachedCurrencies.run {
        ifEmpty {
            loadCurrencies()
                .subscribe(
                    { /* no-op */ },
                    { /* no-op */ }
                )
            this@run
        }
    }

    fun loadCurrencies(): Single<List<CurrencyResponse>> =
        exchangeRepository
            .loadAndCacheCurrencies()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun prices(baseCurrency: String): Single<List<CurrencyPriceModel>> {
        return exchangeRepository
            .getRates(baseCurrency)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
    }

    fun openOrder(
        amount: BigDecimal,
        marketId: Long,
        side: OrderSide,
        type: OrderType,
        price: BigDecimal?
    ): Single<OrderModel> {
        return exchangeRepository
            .openOrder(amount, marketId, side, type, price)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
    }

    fun marketList(): Single<List<MarketModel>> {
        return exchangeRepository
            .getMarketsList()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
    }

    fun getExchangeHistory(dateFrom: String, dateTo: String): Single<List<CurrencyHistory>> =
        exchangeRepository
            .getExchangeHistory(dateFrom, dateTo)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getOrdersList(): Single<OrderWrapperModel> =
        exchangeRepository
            .getOrdersList()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun closeOrder(orderId: Long): Completable =
        exchangeRepository
            .closeOrder(orderId)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}
