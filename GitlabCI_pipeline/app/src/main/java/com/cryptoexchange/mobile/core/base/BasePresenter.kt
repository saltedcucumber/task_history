package com.cryptoexchange.mobile.core.base

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import moxy.MvpView

open class BasePresenter<V : MvpView> : MvpPresenter<V>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    protected fun Disposable.connect() {
        compositeDisposable.add(this)
    }
}
