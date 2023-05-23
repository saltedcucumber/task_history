package com.cryptoexchange.mobile.data.repositories

import com.cryptoexchange.source.entity.user.twofa.enable.start.TwoFaEnableModel
import com.cryptoexchange.source.entrypoint.deeplink.DeepLinkDisableTwoFa
import com.cryptoexchange.source.entrypoint.deeplink.DeepLinkEnableTwoFa
import com.cryptoexchange.source.entrypoint.managers.UserManager
import com.cryptoexchange.mobile.data.storage.Prefs
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class TfaRepository @Inject constructor(
    private val userManager: UserManager,
    private val prefs: Prefs
) {

    fun twoFaStartEnable(password: String): Single<TwoFaEnableModel> =
        Single.fromCallable { userManager.twoFaStartEnable(password) }

    fun twoFaSendConfirmEmail(key: String): Completable =
        Completable.fromAction { userManager.twoFaSendConfirmEmail(key) }

    fun isTfaSuggested(): Single<Boolean> =
        Single.fromCallable {
            val user = userManager.getUser()
            user.userInfo.isGoogleAuthEnabled || prefs.isTfaSuggested
        }

    fun setSuggested(): Completable =
        Completable.fromAction { prefs.isTfaSuggested = true }

    fun twoFaStartEnable(action: DeepLinkEnableTwoFa): Completable =
        Completable.fromCallable { userManager.twoFaConfirmEnable(action) }

    fun twoFaSendDisableEmail(password: String, code: String): Completable =
        Completable.fromAction { userManager.twoFaSendDisableEmail(password, code) }

    fun twoFaConfirmDisable(action: DeepLinkDisableTwoFa): Completable =
        Completable.fromAction { userManager.twoFaConfirmDisable(action) }
}