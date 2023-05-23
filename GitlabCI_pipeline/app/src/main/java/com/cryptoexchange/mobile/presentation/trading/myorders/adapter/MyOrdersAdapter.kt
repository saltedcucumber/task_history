package com.cryptoexchange.mobile.presentation.trading.myorders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.databinding.ItemMyOrdersBinding
import com.cryptoexchange.source.entity.order.OrderModel

class MyOrdersAdapter(
    private val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<MyOrdersViewHolder>() {

    private val orders = mutableListOf<Pair<OrderModel, String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMyOrdersBinding.inflate(inflater, parent, false)

        return MyOrdersViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {
        val pair = orders[position]
        holder.bind(pair.first, pair.second)
    }

    override fun getItemCount(): Int = orders.size

    fun setOrders(orders: List<Pair<OrderModel, String>>) {
        this.orders.clear()
        this.orders.addAll(orders)

        notifyDataSetChanged()
    }
}