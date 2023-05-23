package com.cryptoexchange.mobile.domain.interactor

import com.cryptoexchange.source.entity.user.UserInfo
import com.cryptoexchange.source.entity.user.UserModel
import com.cryptoexchange.source.entrypoint.deeplink.DeepLinkConfirmEmail
import com.cryptoexchange.mobile.core.global.schedulers.SchedulersProvider
import com.cryptoexchange.mobile.data.repositories.UserRepository
import com.cryptoexchange.source.entity.user.activities.UserActivitiesModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val schedulers: SchedulersProvider,
    private val userRepository: UserRepository
) {

    fun getUser(): Single<UserModel> =
        userRepository
            .getUser()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun login(
        email: String,
        password: String
    ): Single<UserModel> =
        userRepository
            .login(email, password)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        referralCode: String
    ): Completable =
        userRepository
            .register(firstName, lastName, email, password, referralCode)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun resendEmail(email: String): Completable =
        userRepository
            .resendEmail(email)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun resetPassword(email: String): Completable =
        userRepository
            .resetPassword(email)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun changePassword(
        code: String,
        newPassword: String,
        oldPassword: String
    ): Completable =
        userRepository
            .changePassword(code, newPassword, oldPassword)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun createNewPassword(
        email: String,
        password: String,
        token: String
    ): Single<UserModel> =
        userRepository
            .createNewPassword(email, password, token)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun confirmEmail(action: DeepLinkConfirmEmail): Completable =
        userRepository
            .confirmEmail(action)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun logout(): Completable =
        userRepository
            .logout()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun twoFaLogin(code: String): Single<UserModel> =
        userRepository
            .twoFaLogin(code)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getUserSettings(): Single<UserInfo> =
        userRepository
            .getUserSettings()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getUserActivities(): Single<UserActivitiesModel> =
        userRepository
            .getUserActivities()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}