package com.cryptoexchange.mobile.presentation.tfa.backupkey

import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.global.ResourceManager
import javax.inject.Inject

class TfaBackupKeyPresenter @Inject constructor(
    private val resourceManager: ResourceManager
) : BasePresenter<TfaBackupKeyView>() {

    private lateinit var backupKey: String

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.showBackupKey(backupKey)
    }

    fun data(backupKey: String) {
        this.backupKey = backupKey
    }

    fun onNextClicked() {
        viewState.navigateToTfaCodeScreen(
            R.id.action_tfaBackupKeyFragment_to_tfaCodeFragment,
            backupKey
        )
    }

    fun onCopyClicked() {
        val result = resourceManager.saveToClipboard(backupKey)
        val resultMessageId = if (result) {
            R.string.copied
        } else {
            R.string.copy_failed
        }

        viewState.showCopyResult(resourceManager.getString(resultMessageId))
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }
}