package com.cryptoexchange.mobile.core

import com.cryptoexchange.mobile.R

sealed class Notification(
    open val background: Int,
    open val textColor: Int,
    open val imageId: Int,
    open val icCloseId: Int
)

class SuccessNotification(
    override val background: Int = R.drawable.message_success,
    override val textColor: Int = R.color.mountainMeadow,
    override val imageId: Int = R.drawable.ic_message_success,
    override val icCloseId: Int = R.drawable.ic_message_close_success
) : Notification(
    background = background,
    textColor = textColor,
    imageId = imageId,
    icCloseId = icCloseId
)

class FailedNotification(
    override val background: Int = R.drawable.message_failed,
    override val textColor: Int = R.color.flamingo,
    override val imageId: Int = R.drawable.ic_message_failed,
    override val icCloseId: Int = R.drawable.ic_message_close_failed
) : Notification(
    background = background,
    textColor = textColor,
    imageId = imageId,
    icCloseId = icCloseId
)

class WarningNotification(
    override val background: Int = R.drawable.message_warning,
    override val textColor: Int = R.color.saffron,
    override val imageId: Int = R.drawable.ic_message_warning,
    override val icCloseId: Int = R.drawable.ic_message_close_warning
) : Notification(
    background = background,
    textColor = textColor,
    imageId = imageId,
    icCloseId = icCloseId
)

class TimeoutNotification(
    override val background: Int = R.drawable.message_timeout,
    override val textColor: Int = R.color.cornflowerBlue,
    override val imageId: Int = R.drawable.ic_message_timeout,
    override val icCloseId: Int = R.drawable.ic_message_close_timeout
) : Notification(
    background = background,
    textColor = textColor,
    imageId = imageId,
    icCloseId = icCloseId
)