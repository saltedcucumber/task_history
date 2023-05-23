package com.cryptoexchange.mobile.presentation.more.transactionhistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.source.entity.withdraw.request.MoneyTransactionModel
import com.cryptoexchange.mobile.databinding.ItemTransactionHistoryBinding

class TransactionHistoryAdapter(
    private val onTransactionClicked: (withdrawId: Long) -> Unit
) : RecyclerView.Adapter<TransactionHistoryViewHolder>() {

    private val transactions = mutableListOf<MoneyTransactionModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTransactionHistoryBinding.inflate(inflater, parent, false)

        return TransactionHistoryViewHolder(binding, onTransactionClicked)
    }

    override fun onBindViewHolder(holder: TransactionHistoryViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    fun setTransactions(transactions: List<MoneyTransactionModel>) {
        val diffCallback = TransactionsDiffUtilCallback(this.transactions, transactions)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.transactions.clear()
        this.transactions.addAll(transactions)

        diffResult.dispatchUpdatesTo(this)
    }
}