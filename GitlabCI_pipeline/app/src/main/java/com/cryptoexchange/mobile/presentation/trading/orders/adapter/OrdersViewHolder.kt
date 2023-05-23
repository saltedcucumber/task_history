package com.cryptoexchange.mobile.presentation.trading.orders.adapter

import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.databinding.ItemTradingOrdersBinding
import com.cryptoexchange.mobile.extensions.clear
import com.cryptoexchange.mobile.extensions.toDisplayStringBy

class OrdersViewHolder(
    private val binding: ItemTradingOrdersBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: OrdersAdapter.OrderBindingData) {
        with(binding) {
            val buyModelOptional = model.buyPair

            if (buyModelOptional.isPresent) {
                volumeBuyValueTextView.text = buyModelOptional.get().volume.toDisplayStringBy()
                priceBuyValueTextView.text = buyModelOptional.get().price.toDisplayStringBy()
            } else {
                volumeBuyValueTextView.clear()
                priceBuyValueTextView.clear()
            }

            val sellModelOptional = model.sellPair
            if (sellModelOptional.isPresent) {
                volumeSellValueTextView.text = sellModelOptional.get().volume.toDisplayStringBy()
                priceSellValueTextView.text = sellModelOptional.get().price.toDisplayStringBy()
            } else {
                volumeSellValueTextView.clear()
                priceSellValueTextView.clear()
            }
        }
    }
}