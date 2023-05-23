package com.cryptoexchange.mobile.app

import android.app.Application
import android.util.Log
import com.cryptoexchange.mobile.BuildConfig
import com.cryptoexchange.mobile.di.AppInjector
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppInjector.initComponent(this)
        initRxJavaErrorHandler()
    }

    private fun initRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler {
            if (it !is UndeliverableException) {
                throw it
            } else if (BuildConfig.DEBUG) {
                Log.w(TAG_RX_JAVA_ERROR_HANDLER, it)
            }
        }
    }

    companion object {
        private const val TAG_RX_JAVA_ERROR_HANDLER = "TAG_RX_JAVA_ERROR_HANDLER"
    }
}
