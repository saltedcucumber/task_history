package com.cryptoexchange.mobile.domain.interactor.market

import com.cryptoexchange.source.entity.market.MarketModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class TradingMarketInteractorImpl @Inject constructor() : TradingMarketInteractor {

    private val loadSubject = BehaviorSubject.create<MarketModel>()

    override fun marketObservable(): Observable<MarketModel> {
        return loadSubject
            .serialize()
            .distinctUntilChanged()
    }

    override fun push(model: MarketModel) {
        loadSubject.onNext(model)
    }
}
