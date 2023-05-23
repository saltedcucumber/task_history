package com.cryptoexchange.mobile.di

import android.content.Context

object AppInjector {

    private lateinit var appComponent: AppComponent

    internal fun initComponent(
        applicationContext: Context
    ) {
        if (!::appComponent.isInitialized) {
            recreateComponent(applicationContext)
        }
    }

    fun recreateComponent(
        applicationContext: Context
    ) {
        appComponent = DaggerAppComponent
            .builder()
            .context(applicationContext)
            .build()
    }

    internal fun getComponent() = appComponent
}