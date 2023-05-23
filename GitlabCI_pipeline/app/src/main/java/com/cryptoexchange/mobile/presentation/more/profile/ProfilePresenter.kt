package com.cryptoexchange.mobile.presentation.more.profile

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.MessageType
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.base.ErrorHandler
import com.cryptoexchange.mobile.domain.interactor.UserInteractor
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<ProfileView>() {

    fun onChangePasswordClicked() {
        viewState.navigateTo(R.id.changePasswordFragment)
    }

    fun onTwoFAClicked() {
        userInteractor
            .getUser()
            .subscribe(
                { resolve2FANavigationBy(it.userInfo.isGoogleAuthEnabled) },
                {
                    errorHandler.handleError(it) { message ->
                        viewState.showMessage(message, MessageType.FAILED)
                    }
                }
            )
            .connect()
    }

    private fun resolve2FANavigationBy(is2FAEnabled: Boolean) {
        val actionId = if (is2FAEnabled) {
            R.id.action_profileFragment_to_twoFAStatusFragment
        } else {
            R.id.action_profileFragment_to_two_fa_enabling_graph
        }
        viewState.navigateTo(actionId)
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }
}
