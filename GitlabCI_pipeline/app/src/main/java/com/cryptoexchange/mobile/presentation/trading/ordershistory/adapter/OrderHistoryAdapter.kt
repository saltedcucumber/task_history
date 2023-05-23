package com.cryptoexchange.mobile.presentation.trading.ordershistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.databinding.ItemTradingOrderHistoryBinding
import com.cryptoexchange.source.entity.trade.history.recent.TradeRecentOrderHistoryModel

class OrderHistoryAdapter(
    private val data: List<TradeRecentOrderHistoryModel>,
) : RecyclerView.Adapter<OrderHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTradingOrderHistoryBinding.inflate(inflater, parent, false)

        return OrderHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}
