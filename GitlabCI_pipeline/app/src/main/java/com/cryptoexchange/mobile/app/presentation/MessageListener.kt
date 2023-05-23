package com.cryptoexchange.mobile.app.presentation

import com.cryptoexchange.mobile.core.Notification

interface MessageListener {

    fun showMessage(textId: Int, notification: Notification)

    fun showMessage(message: String, notification: Notification)
}