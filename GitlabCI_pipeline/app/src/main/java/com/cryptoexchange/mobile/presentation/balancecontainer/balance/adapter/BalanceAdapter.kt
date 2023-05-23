package com.cryptoexchange.mobile.presentation.balancecontainer.balance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.source.entity.wallet.WalletModel
import com.cryptoexchange.mobile.databinding.ItemBalanceBinding

class BalanceAdapter(
    private val depositClickListener: (id: Long) -> Unit,
    private val withdrawClickListener: (id: Long) -> Unit
) : RecyclerView.Adapter<BalanceViewHolder>() {

    private val walletModels = mutableListOf<WalletModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBalanceBinding.inflate(inflater, parent, false)

        return BalanceViewHolder(
            binding,
            { depositClickListener.invoke(walletModels[it].id) },
            { withdrawClickListener.invoke(walletModels[it].id) }
        )
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.bind(walletModels[position])
    }

    override fun getItemCount(): Int = walletModels.size

    fun setCurrencies(walletModels: List<WalletModel>) {
        val diffCallback = BalanceDiffUtilCallback(this.walletModels, walletModels)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.walletModels.clear()
        this.walletModels.addAll(walletModels)

        diffResult.dispatchUpdatesTo(this)
    }
}