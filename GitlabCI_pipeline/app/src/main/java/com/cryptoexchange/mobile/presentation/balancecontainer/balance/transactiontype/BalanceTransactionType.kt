package com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype

import android.os.Parcelable
import com.cryptoexchange.source.entity.wallet.WalletModel

sealed class BalanceTransactionType(
    open val wallet: WalletModel,
    open val tokensRemains: String
) : Parcelable
