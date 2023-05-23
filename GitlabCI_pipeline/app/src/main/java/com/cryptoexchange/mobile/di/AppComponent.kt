package com.cryptoexchange.mobile.di

import android.content.Context
import com.cryptoexchange.mobile.di.module.AppModule
import com.cryptoexchange.mobile.di.module.DataModule
import com.cryptoexchange.mobile.di.module.DomainModule
import com.cryptoexchange.mobile.di.module.SubcomponentsModule
import com.cryptoexchange.mobile.di.subcomponent.ActivityComponent
import com.cryptoexchange.mobile.di.subcomponent.FragmentComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DomainModule::class,
        DataModule::class,
        SubcomponentsModule::class
    ]
)
interface AppComponent {

    fun activityComponent(): ActivityComponent.Factory

    fun fragmentComponent(): FragmentComponent.Factory

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun build(): AppComponent
    }
}