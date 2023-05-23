package com.cryptoexchange.mobile.presentation.trading.ordershistory.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.databinding.ItemTradingOrderHistoryBinding
import com.cryptoexchange.mobile.extensions.DD_MM_HH_MM
import com.cryptoexchange.mobile.extensions.toDateString
import com.cryptoexchange.mobile.extensions.toDisplayStringBy
import com.cryptoexchange.source.entity.order.OrderSide
import com.cryptoexchange.source.entity.trade.history.recent.TradeRecentOrderHistoryModel

class OrderHistoryViewHolder(
    private val binding: ItemTradingOrderHistoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: TradeRecentOrderHistoryModel) {
        with(binding) {
            priceValueTextView.apply {
                text = model.price.toDisplayStringBy()
                val colorRes = when (model.side) {
                    OrderSide.BUY -> R.color.shamrock
                    OrderSide.SELL -> R.color.bittersweet
                    else -> R.color.tundora
                }
                setTextColor(ContextCompat.getColor(context, colorRes))
            }

            amountValueTextView.text = model.amount.toDisplayStringBy()
            dateValueTextView.text = model.matchedAt.toDateString(DD_MM_HH_MM)
        }
    }
}
