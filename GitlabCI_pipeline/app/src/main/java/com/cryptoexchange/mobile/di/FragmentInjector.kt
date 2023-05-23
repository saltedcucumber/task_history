package com.cryptoexchange.mobile.di

import com.cryptoexchange.mobile.di.subcomponent.FragmentComponent

object FragmentInjector {

    private lateinit var fragmentComponent: FragmentComponent

    internal fun initComponent() {
        fragmentComponent = AppInjector.getComponent()
            .fragmentComponent()
            .create()
    }

    internal fun getComponent() = fragmentComponent
}