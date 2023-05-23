package com.cryptoexchange.mobile.di.module

import com.cryptoexchange.mobile.di.subcomponent.ActivityComponent
import com.cryptoexchange.mobile.di.subcomponent.FragmentComponent
import dagger.Module

@Module(subcomponents = [ActivityComponent::class, FragmentComponent::class])
interface SubcomponentsModule