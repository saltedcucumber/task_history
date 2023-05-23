package com.cryptoexchange.mobile.presentation.more.transactionhistory.adapter

import androidx.recyclerview.widget.DiffUtil
import com.cryptoexchange.source.entity.withdraw.request.MoneyTransactionModel

class TransactionsDiffUtilCallback(
    private val oldList: List<MoneyTransactionModel>,
    private val newList: List<MoneyTransactionModel>
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

        return old.id == new.id
    }
}