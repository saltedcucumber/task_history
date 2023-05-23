package com.cryptoexchange.mobile.data.repositories

import com.cryptoexchange.source.entity.UnauthorizedException
import com.cryptoexchange.mobile.domain.entity.socket.LoginMessage
import com.cryptoexchange.mobile.domain.entity.socket.MessageType
import com.cryptoexchange.mobile.domain.entity.socket.SocketEvent
import com.cryptoexchange.mobile.domain.entity.socket.SocketMessage
import com.cryptoexchange.mobile.data.socket.AppSocket
import com.cryptoexchange.mobile.data.socket.WebSocketState
import com.cryptoexchange.source.preferences.SourcePrefs
import com.cryptoexchange.mobile.core.global.schedulers.SchedulersProvider
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSocketRepository @Inject constructor(
    private val appSocket: AppSocket,
    private val sourcePrefs: SourcePrefs,
    schedulers: SchedulersProvider
) {

    // just for development
    private val url = ""

    private val messageSubject = PublishSubject.create<SocketEvent<*>>()

    init {
        var disposable: Disposable? = null
        disposable = appSocket
            .observeSocketMessages()
            .subscribeOn(schedulers.io())
            .doAfterTerminate { disposable?.dispose() }
            .subscribe(
                { handleSocketMessage(it) },
                { }
            )
    }

    fun connectToAppSocket(): Completable =
        appSocket
            .connect(url)
            .filter { it == WebSocketState.CONNECTED }
            .take(1)
            .ignoreElements()
            .andThen {
                val accountId = sourcePrefs.user?.userInfo?.accountList?.firstOrNull()
                val token = sourcePrefs.user?.token
                if (accountId != null && token != null) {
                    val loginMessage = LoginMessage(accountId, token)
                    val message = SocketMessage(
                        MessageType.LOGIN,
                        loginMessage
                    )
                    appSocket.sendMessage(message)
                    it.onComplete()
                } else {
                    it.onError(UnauthorizedException("Token or accountId are null"))
                }
            }

    fun observeMessages(): Observable<SocketEvent<*>> = messageSubject.hide()

    private fun handleSocketMessage(event: SocketEvent<*>) {
        messageSubject.onNext(event)
    }
}
