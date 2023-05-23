package com.cryptoexchange.mobile.data.repositories

import com.cryptoexchange.source.entity.market.MarketModel
import com.cryptoexchange.source.entrypoint.managers.MarketManager
import io.reactivex.rxjava3.core.Single

class MarketRepository(
    private val marketManager: MarketManager
) {

    fun getMarkets(): Single<List<MarketModel>> =
        Single.fromCallable { marketManager.getMarketsList() }
}