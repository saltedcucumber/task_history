package com.cryptoexchange.mobile.presentation.more.profile.twofastatus

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BasePresenter
import javax.inject.Inject

class TwoFAStatusPresenter @Inject constructor() : BasePresenter<TwoFAStatusView>() {

    fun onDisableClicked() {
        viewState.navigateToTwoFADisabling(
            R.id.action_twoFAStatusFragment_to_two_fa_disabling_graph
        )
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }
}
