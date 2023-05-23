package com.cryptoexchange.mobile.core

enum class MessageType {
    SUCCESS,
    FAILED,
    WARNING,
    TIMEOUT;

    companion object {

        fun getNotificationBy(messageType: MessageType) = when (messageType) {
            SUCCESS -> SuccessNotification()
            FAILED -> FailedNotification()
            WARNING -> WarningNotification()
            TIMEOUT -> TimeoutNotification()
        }
    }
}
