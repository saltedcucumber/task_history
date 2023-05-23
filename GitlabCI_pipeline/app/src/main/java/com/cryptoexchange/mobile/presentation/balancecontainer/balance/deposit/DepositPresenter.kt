package com.cryptoexchange.mobile.presentation.balancecontainer.balance.deposit

import android.content.Context
import android.widget.Toast
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BasePresenter
import com.cryptoexchange.mobile.core.global.ResourceManager
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.DefaultDepositType
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.DepositType
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.MemoDepositType
import javax.inject.Inject

class DepositPresenter @Inject constructor(
    private val context: Context,
    private val resourceManager: ResourceManager
) : BasePresenter<DepositView>() {

    lateinit var depositType: DepositType

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        updateScreen()
    }

    fun data(depositType: DepositType) {
        this.depositType = depositType
    }

    fun onClickCopyToClipboard(copyText: String) {
        resourceManager.saveToClipboard(copyText)
        Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show()
    }

    private fun updateScreen() {
        with(depositType) {
            when (this) {
                is DefaultDepositType -> onDefaultDepositType(this)
                is MemoDepositType -> onMemoDepositType(this)
            }
            viewState.updateUIWithArguments(this)
        }
    }

    private fun onMemoDepositType(type: MemoDepositType) {
        with(viewState) {
            showReceiveAddress()
            showReceiveMemo()
            updateReceiveAddress(type.receiveAddress)
            updateReceiveMemo(type.memo)
        }
    }

    private fun onDefaultDepositType(type: DefaultDepositType) {
        with(viewState) {
            showReceiveAddress()
            updateReceiveAddress(type.receiveAddress)
        }
    }

    fun onBackPressed() {
        viewState.navigateBack()
    }
}