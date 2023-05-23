package com.cryptoexchange.mobile.domain.interactor

import com.cryptoexchange.source.entity.user.twofa.enable.start.TwoFaEnableModel
import com.cryptoexchange.source.entrypoint.deeplink.DeepLinkDisableTwoFa
import com.cryptoexchange.source.entrypoint.deeplink.DeepLinkEnableTwoFa
import com.cryptoexchange.mobile.core.global.schedulers.SchedulersProvider
import com.cryptoexchange.mobile.data.repositories.TfaRepository
import com.cryptoexchange.mobile.data.repositories.UserRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class TfaInteractor @Inject constructor(
    private val tfaRepository: TfaRepository,
    private val schedulers: SchedulersProvider,
    private val userRepository: UserRepository
) {

    fun twoFaStartEnable(password: String): Single<TwoFaEnableModel> =
        tfaRepository
            .twoFaStartEnable(password)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun twoFaSendConfirmEmail(key: String): Completable =
        tfaRepository
            .twoFaSendConfirmEmail(key)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun isTfaSuggested(): Single<Boolean> =
        tfaRepository
            .isTfaSuggested()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun setSuggested(): Completable =
        tfaRepository
            .setSuggested()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun twoFaStartEnable(action: DeepLinkEnableTwoFa): Completable =
        tfaRepository
            .twoFaStartEnable(action)
            .andThen(userRepository.getUserSettings())
            .ignoreElement()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun twoFaSendDisableEmail(password: String, code: String): Completable =
        tfaRepository
            .twoFaSendDisableEmail(password, code)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun twoFaConfirmDisable(action: DeepLinkDisableTwoFa): Completable =
        tfaRepository
            .twoFaConfirmDisable(action)
            .andThen(userRepository.getUserSettings())
            .ignoreElement()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}