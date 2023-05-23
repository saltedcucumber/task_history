package com.cryptoexchange.mobile.di.subcomponent

import com.cryptoexchange.mobile.app.presentation.AppActivity
import com.cryptoexchange.mobile.di.qualifiers.RestUrl
import dagger.Subcomponent

@Subcomponent
interface ActivityComponent {

    fun inject(activity: AppActivity)

    @RestUrl
    fun restUrl(): String

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }
}