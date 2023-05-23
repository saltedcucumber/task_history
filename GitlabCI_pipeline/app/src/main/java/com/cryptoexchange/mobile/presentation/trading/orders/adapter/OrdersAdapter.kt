package com.cryptoexchange.mobile.presentation.trading.orders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.databinding.ItemTradingOrdersBinding
import com.cryptoexchange.source.entity.trade.orders.MarketOrderbookModel
import com.cryptoexchange.source.entity.trade.orders.OrderbookValueModel
import java.util.*

class OrdersAdapter : RecyclerView.Adapter<OrdersViewHolder>() {

    private val bindingData = mutableListOf<OrderBindingData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTradingOrdersBinding.inflate(inflater, parent, false)

        return OrdersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        holder.bind(bindingData[position])
    }

    override fun getItemCount(): Int = bindingData.size

    fun setOrders(data: MarketOrderbookModel) {
        bindingData.clear()
        bindingData.addAll(data.toBindingData())

        notifyDataSetChanged()
    }

    fun clear() {
        bindingData.clear()
        notifyDataSetChanged()
    }

    private fun MarketOrderbookModel.toBindingData(): List<OrderBindingData> {
        val result = mutableListOf<OrderBindingData>()
        val size = maxOf(buyOrderbookList.size, sellOrderbookList.size)
        for (i in 0 until size) {
            val buy = Optional.ofNullable(buyOrderbookList.getOrNull(i))
            val sell = Optional.ofNullable(sellOrderbookList.getOrNull(i))
            result.add(
                OrderBindingData(buy, sell)
            )
        }

        return result
    }

    data class OrderBindingData(
        val buyPair: Optional<OrderbookValueModel>,
        val sellPair: Optional<OrderbookValueModel>,
    )
}
