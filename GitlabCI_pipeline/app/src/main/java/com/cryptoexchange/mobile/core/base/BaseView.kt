package com.cryptoexchange.mobile.core.base

import com.cryptoexchange.mobile.core.MessageType
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface BaseView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(textId: Int, messageType: MessageType)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String, messageType: MessageType)
}