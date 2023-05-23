package com.cryptoexchange.mobile.presentation.more.tradinghistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cryptoexchange.mobile.databinding.ItemTradingHistoryBinding
import com.cryptoexchange.mobile.domain.entity.TradingHistory

class TradingHistoryAdapter(
    private val detailsClickListener: (Int) -> Unit
) : ListAdapter<TradingHistory, TradingHistoryViewHolder>(
    TradingDiffUtilCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TradingHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTradingHistoryBinding.inflate(inflater, parent, false)

        return TradingHistoryViewHolder(binding, detailsClickListener)
    }

    override fun onBindViewHolder(holder: TradingHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}