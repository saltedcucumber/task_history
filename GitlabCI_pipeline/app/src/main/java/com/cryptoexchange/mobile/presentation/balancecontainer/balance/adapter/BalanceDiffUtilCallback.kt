package com.cryptoexchange.mobile.presentation.balancecontainer.balance.adapter

import androidx.recyclerview.widget.DiffUtil
import com.cryptoexchange.source.entity.wallet.WalletModel

class BalanceDiffUtilCallback(
    private val oldList: List<WalletModel>,
    private val newList: List<WalletModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]

        return old == new
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]

        return old.id == new.id && old.currencyShortName == new.currencyShortName
    }
}