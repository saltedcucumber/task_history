package com.cryptoexchange.mobile.domain.interactor

import com.cryptoexchange.source.entity.trade.FeeSetAccountValueResponse
import com.cryptoexchange.mobile.core.global.schedulers.SchedulersProvider
import com.cryptoexchange.mobile.data.repositories.MarketRepository
import com.cryptoexchange.mobile.data.repositories.TradeRepository
import com.cryptoexchange.mobile.domain.entity.TradingHistory
import com.cryptoexchange.source.entity.market.MarketModel
import com.cryptoexchange.source.entity.trade.history.recent.TradeRecentOrderHistoryModel
import com.cryptoexchange.source.entity.trade.orders.MarketOrderbookModel
import com.cryptoexchange.source.entity.trade.orders.OrderQueries
import com.cryptoexchange.source.entity.trade.OhlcvHistoryModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class TradeInteractor @Inject constructor(
    private val tradeRepository: TradeRepository,
    private val marketRepository: MarketRepository,
    private val schedulers: SchedulersProvider
) {

    fun getTradeHistory(
        limitQuery: Int,
        offsetQuery: Int,
        dateFromQuery: String? = null,
        dateToQuery: String? = null,
        sideQuery: String? = null,
        typeQuery: String? = null
    ): Single<List<TradingHistory>> =
        Single.zip(
            tradeRepository.getTradeHistory(
                limitQuery,
                offsetQuery,
                dateFromQuery,
                dateToQuery,
                sideQuery,
                typeQuery
            ),
            marketRepository.getMarkets()
        ) { histories, markets ->
            histories.elements.map { history ->
                val market = markets.find { history.marketId == it.id }
                    ?: throw IllegalArgumentException(
                        "Market for marketId:${history.marketId} not found"
                    )

                with(history) {
                    TradingHistory(
                        tradeId = id,
                        leftCurrencyCode = market.leftCurrencyCode,
                        rightCurrencyCode = market.rightCurrencyCode,
                        amount = amount,
                        price = price,
                        date = matchedAt,
                        side = side,
                        orderId = orderId,
                        type = type,
                        fee = fee
                    )
                }
            }
        }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getCommissions(): Single<List<FeeSetAccountValueResponse>> =
        tradeRepository
            .getAccountsList()
            .map { list -> list.map { it.feeSetValue } }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getRecentTrades(id: Long): Single<List<TradeRecentOrderHistoryModel>> =
        tradeRepository
            .getRecentTrades(id)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getMarkets(): Single<List<MarketModel>> =
        marketRepository
            .getMarkets()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getOrderbookList(orderQueries: OrderQueries): Single<MarketOrderbookModel> =
        tradeRepository
            .getOrderbookList(orderQueries)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getOhlcvHistory(marketId: Long, dateFrom: Long, dateTo: Long): Single<OhlcvHistoryModel> =
        tradeRepository
            .getOhlcvHistory(marketId, dateFrom, dateTo)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}
