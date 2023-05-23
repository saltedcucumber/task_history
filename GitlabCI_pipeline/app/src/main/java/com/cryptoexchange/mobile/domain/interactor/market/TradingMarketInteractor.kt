package com.cryptoexchange.mobile.domain.interactor.market

import com.cryptoexchange.source.entity.market.MarketModel
import io.reactivex.rxjava3.core.Observable

interface TradingMarketInteractor {

    fun push(model: MarketModel)

    fun marketObservable(): Observable<MarketModel>
}
