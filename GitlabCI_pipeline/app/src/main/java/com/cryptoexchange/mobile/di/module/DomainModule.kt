package com.cryptoexchange.mobile.di.module

import com.cryptoexchange.mobile.core.global.schedulers.AppSchedulers
import com.cryptoexchange.mobile.core.global.schedulers.SchedulersProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideSchedulersProvider(): SchedulersProvider = AppSchedulers()
}
