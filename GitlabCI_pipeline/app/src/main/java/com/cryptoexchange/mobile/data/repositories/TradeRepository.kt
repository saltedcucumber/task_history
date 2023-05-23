package com.cryptoexchange.mobile.data.repositories

import com.cryptoexchange.source.entity.trade.AccountResponse
import com.cryptoexchange.source.entity.trade.OhlcvHistoryModel
import com.cryptoexchange.source.entity.trade.history.TradeHistoriesModel
import com.cryptoexchange.source.entity.trade.history.recent.TradeRecentOrderHistoryModel
import com.cryptoexchange.source.entity.trade.orders.MarketOrderbookModel
import com.cryptoexchange.source.entity.trade.orders.OrderQueries
import com.cryptoexchange.source.entrypoint.managers.AccountManager
import com.cryptoexchange.source.entrypoint.managers.TradeManager
import io.reactivex.rxjava3.core.Single

class TradeRepository(
    private val tradeManager: TradeManager,
    private val accountManager: AccountManager
) {

    fun getTradeHistory(
        limit: Int,
        offset: Int,
        dateFrom: String?,
        dateTo: String?,
        side: String?,
        type: String?
    ): Single<TradeHistoriesModel> =
        Single.fromCallable {
            tradeManager.getUserTradeHistory(limit, offset, dateFrom, dateTo, side, type)
        }

    fun getAccountsList(): Single<List<AccountResponse>> =
        Single.fromCallable {
            accountManager.getAccountsList()
        }

    // TODO market DI
    fun getRecentTrades(id: Long): Single<List<TradeRecentOrderHistoryModel>> =
        Single.fromCallable {
            tradeManager.getRecentTrades(id)
        }

    fun getOhlcvHistory(marketId: Long, dateFrom: Long, dateTo: Long): Single<OhlcvHistoryModel> =
        Single.fromCallable {
            tradeManager.getOhlcvHistory(marketId, dateFrom, dateTo)
        }

    fun getOrderbookList(orderQueries: OrderQueries): Single<MarketOrderbookModel> =
        Single.fromCallable {
            tradeManager.getOrderbookList(orderQueries)
        }
}
