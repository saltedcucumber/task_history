package com.cryptoexchange.mobile.core.base

import android.os.Bundle
import android.view.View
import com.cryptoexchange.mobile.app.presentation.MessageListener
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.extensions.addOnBackPressedCallback
import moxy.MvpAppCompatFragment

abstract class BaseFragment : MvpAppCompatFragment(), BaseView {

    open fun setupUI() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun showMessage(textId: Int, messageType: MessageType) {
        val notification = MessageType.getNotificationBy(messageType)
        getMessageListener().showMessage(textId, notification)
    }

    override fun showMessage(message: String, messageType: MessageType) {
        val notification = MessageType.getNotificationBy(messageType)
        getMessageListener().showMessage(message, notification)
    }

    private fun getMessageListener(): MessageListener =
        requireActivity() as? MessageListener ?: throw IllegalArgumentException(
            "${requireActivity()::class.java.name} should implement MessageListener"
        )

    protected fun registerOnBackPressedListener(callback: () -> Unit) {
        addOnBackPressedCallback(callback)
    }
}
