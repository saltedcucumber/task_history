package com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype

import android.os.Parcelable
import com.cryptoexchange.source.entity.wallet.WalletModel
import kotlinx.parcelize.Parcelize

sealed class WithdrawType(
    wallet: WalletModel,
    tokensRemains: String,
    open val currencyId: Long
) : BalanceTransactionType(wallet, tokensRemains)

@Parcelize
data class DefaultWithdrawType(
    override val wallet: WalletModel,
    override val tokensRemains: String,
    override val currencyId: Long,
) : WithdrawType(wallet, tokensRemains, currencyId), Parcelable

@Parcelize
data class MemoWithdrawType(
    override val wallet: WalletModel,
    override val tokensRemains: String,
    override val currencyId: Long,
) : WithdrawType(wallet, tokensRemains, currencyId), Parcelable
