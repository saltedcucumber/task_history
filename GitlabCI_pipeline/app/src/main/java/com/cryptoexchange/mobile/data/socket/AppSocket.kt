package com.cryptoexchange.mobile.data.socket

import com.cryptoexchange.mobile.domain.entity.socket.SocketEvent
import com.cryptoexchange.mobile.domain.entity.socket.SocketMessage
import com.cryptoexchange.mobile.extensions.fromJsonToSocketEvent
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

@Singleton
class AppSocket @Inject constructor(
    private val gson: Gson
) : WebSocketListener() {

    private var webSocket: WebSocket? = null

    private val webSocketStateSubject = BehaviorSubject.createDefault(WebSocketState.CLOSED)
    private val webSocketMessageSubject = PublishSubject.create<SocketEvent<*>>()

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        webSocketStateSubject.onNext(WebSocketState.CONNECTED)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)

        try {
            val message = gson.fromJsonToSocketEvent(text)
            webSocketMessageSubject.onNext(message)
        } catch (jse: JsonSyntaxException) {
            webSocketMessageSubject.onError(jse)
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)

        webSocketStateSubject.onNext(WebSocketState.CLOSED)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)

        webSocketStateSubject.onNext(WebSocketState.CLOSING)
    }

    fun connect(url: String): Observable<WebSocketState> {
        val client = OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            // TODO we should receive ping interval from application
            .pingInterval(PING_INTERVAL, TimeUnit.MILLISECONDS)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, this)
        webSocketStateSubject.onNext(WebSocketState.CONNECTING)

        return webSocketStateSubject.hide()
    }

    fun observeSocketMessages(): Observable<SocketEvent<*>> =
        webSocketMessageSubject.hide()

    fun sendMessage(message: SocketMessage<*>) {
        val messageJson = gson.toJson(message)

        webSocket?.send(messageJson)
    }

    companion object {
        private const val PING_INTERVAL = 30000L
    }
}
