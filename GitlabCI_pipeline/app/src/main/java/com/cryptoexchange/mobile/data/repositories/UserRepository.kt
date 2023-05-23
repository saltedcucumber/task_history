package com.cryptoexchange.mobile.data.repositories

import com.cryptoexchange.mobile.data.storage.Prefs
import com.cryptoexchange.source.entity.user.UserInfo
import com.cryptoexchange.source.entity.user.UserModel
import com.cryptoexchange.source.entity.user.activities.UserActivitiesModel
import com.cryptoexchange.source.entrypoint.deeplink.DeepLinkConfirmEmail
import com.cryptoexchange.source.entrypoint.managers.UserManager
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserRepository(
    private val userManager: UserManager,
    private val prefs: Prefs
) {

    fun getUser(): Single<UserModel> = Single.fromCallable { userManager.getUser() }

    fun login(
        email: String,
        password: String
    ): Single<UserModel> =
        Single.fromCallable {
            userManager.login(email, password)
        }

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        referralCode: String
    ): Completable =
        Completable.fromCallable {
            userManager.register(firstName, lastName, email, password, referralCode)
        }

    fun resendEmail(email: String): Completable =
        Completable
            .fromAction { userManager.resendEmail(email) }

    fun resetPassword(email: String): Completable =
        Completable.fromAction {
            userManager.resetPassword(email)
        }

    fun changePassword(
        code: String,
        newPassword: String,
        oldPassword: String
    ): Completable =
        Completable.fromAction {
            userManager.changePassword(code, newPassword, oldPassword)
            prefs.clear()
            userManager.logout()
        }

    fun createNewPassword(
        email: String,
        password: String,
        token: String
    ): Single<UserModel> =
        Single.fromCallable {
            userManager.createNewPassword(email, password, token)
        }

    fun confirmEmail(action: DeepLinkConfirmEmail): Completable =
        Completable.fromAction {
            userManager.confirmEmail(action)
        }

    fun logout(): Completable =
        Completable.fromAction {
            prefs.clear()
            userManager.logout()
        }

    fun twoFaLogin(code: String): Single<UserModel> =
        Single.fromCallable { userManager.twoFaLogin(code, userManager.getUser().token) }

    fun getUserSettings(): Single<UserInfo> =
        Single.fromCallable {
            userManager.getUserSettings()
        }

    fun getUserActivities(): Single<UserActivitiesModel> =
        Single.fromCallable {
            userManager.getUserActivities()
        }
}