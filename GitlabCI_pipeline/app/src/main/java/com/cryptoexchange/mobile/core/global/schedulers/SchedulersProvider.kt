package com.cryptoexchange.mobile.core.global.schedulers

import io.reactivex.rxjava3.core.Scheduler

interface SchedulersProvider {
    fun ui(): Scheduler
    fun computation(): Scheduler
    fun trampoline(): Scheduler
    fun newThread(): Scheduler
    fun io(): Scheduler
}
