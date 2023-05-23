package com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype

import android.os.Parcelable
import com.cryptoexchange.source.entity.wallet.WalletModel
import kotlinx.parcelize.Parcelize

sealed class DepositType(
    wallet: WalletModel,
    tokensRemains: String,
) : BalanceTransactionType(wallet, tokensRemains)

@Parcelize
data class DefaultDepositType(
    override val wallet: WalletModel,
    override val tokensRemains: String,
    val receiveAddress: String
) : DepositType(wallet, tokensRemains), Parcelable

@Parcelize
data class MemoDepositType(
    override val wallet: WalletModel,
    override val tokensRemains: String,
    val receiveAddress: String,
    val memo: String
) : DepositType(wallet, tokensRemains), Parcelable
