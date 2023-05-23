package com.cryptoexchange.mobile.app.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.cryptoexchange.mobile.BuildConfig
import com.cryptoexchange.mobile.core.Notification
import com.cryptoexchange.mobile.di.AppInjector
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.di.subcomponent.ActivityComponent
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.databinding.ActivityAppBinding
import com.cryptoexchange.mobile.extensions.setupAppSnackBar
import com.cryptoexchange.source.entrypoint.SDKConfig
import com.cryptoexchange.source.entrypoint.SdkEntryPoint
import com.cryptoexchange.source.entrypoint.UnauthorizedListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

class AppActivity : MvpAppCompatActivity(), AppView, MessageListener, UnauthorizedListener {

    private lateinit var activityComponent: ActivityComponent

    private lateinit var binding: ActivityAppBinding

    @Inject
    lateinit var presenterProvider: Provider<AppPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onCreate(savedInstanceState: Bundle?) {
        initDI()
        initSdk()
        setTheme(R.style.Theme_AppTheme)
        super.onCreate(savedInstanceState)

        binding = ActivityAppBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        presenter.onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        presenter.onNewIntent(intent)
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun setupStartDestination(id: Int) {
        val navController = getNavHostFragment(R.id.appNavHostFragment)?.navController ?: return
        navController.apply {
            graph = navInflater.inflate(R.navigation.unauthorized_graph).apply {
                setStartDestination(id)
            }
        }
    }

    override fun navigateToCreatePassword(bundle: Bundle) {
        val navController = getNavHostFragment(R.id.appNavHostFragment)?.navController ?: return
        navController.apply {
            graph = navInflater.inflate(R.navigation.unauthorized_graph)
            navigate(R.id.createPasswordFragment, bundle)
        }
    }

    override fun navigateToWelcome() {
        val controller = Navigation.findNavController(this, R.id.appNavHostFragment)
        controller.popBackStack(R.id.activity_graph, true)
        controller.navigate(R.id.welcomeFragment)
    }

    override fun showMessage(textId: Int, notification: Notification) {
        val message = getString(textId)
        showMessage(message, notification)
    }

    override fun showMessage(message: String, notification: Notification) {
        val messageView = getMessageView(message, notification)
        val snackbar = getSnackBar(messageView)

        val close = messageView.findViewById<ImageView>(R.id.messageClose)
        close.setOnClickListener { snackbar.dismiss() }
        snackbar.show()
    }

    override fun navigateToBalance(destinationId: Int) {
        val controller = Navigation.findNavController(this, R.id.mainNavHostFragment)
        controller.popBackStack(R.id.dashboardDestination, false)
        controller.navigate(R.id.balanceDestination)
    }

    override fun onSessionExpired() {
        presenter.onSessionExpired()
    }

    override fun onDestroy() {
        super.onDestroy()

        SdkEntryPoint.dependencyManager.userManager.removeUnauthorizedListener(this)
    }

    private fun initDI() {
        activityComponent = AppInjector.getComponent().activityComponent().create().also {
            it.inject(this)
        }
        FragmentInjector.initComponent()
    }

    private fun initSdk() {
        val config = SDKConfig(this, activityComponent.restUrl(), "")
        SdkEntryPoint.setupSDK(config)
        SdkEntryPoint.dependencyManager.userManager.setUnauthorizedListener(this)
    }

    private fun getNavHostFragment(@IdRes id: Int): NavHostFragment? {
        return supportFragmentManager.findFragmentById(id) as? NavHostFragment
    }

    private fun getMessageView(
        message: String,
        notification: Notification
    ): View {
        val messageView = layoutInflater.inflate(R.layout.item_message, null)

        val image = messageView.findViewById<ImageView>(R.id.messageIcon)
        val close = messageView.findViewById<ImageView>(R.id.messageClose)
        val text = messageView.findViewById<TextView>(R.id.messageText)

        with(notification) {
            text.text = message
            messageView.background = ContextCompat.getDrawable(this@AppActivity, background)
            text.setTextColor(ContextCompat.getColor(this@AppActivity, textColor))
            image.setImageResource(imageId)
            close.setImageResource(icCloseId)
        }

        messageView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {

                override fun onGlobalLayout() {
                    messageView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    messageView.updatePadding(top = 0)
                }
            })

        return messageView
    }

    private fun getSnackBar(messageView: View): Snackbar {
        val duration = if (BuildConfig.DEBUG) {
            BaseTransientBottomBar.LENGTH_INDEFINITE
        } else {
            BaseTransientBottomBar.LENGTH_LONG
        }
        val snackbar = Snackbar.make(binding.root, "", duration)

        snackbar.setupAppSnackBar().apply {
            addView(messageView, 0)
        }

        return snackbar
    }
}
