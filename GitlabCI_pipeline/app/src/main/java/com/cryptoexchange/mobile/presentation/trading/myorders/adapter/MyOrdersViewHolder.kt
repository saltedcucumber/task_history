package com.cryptoexchange.mobile.presentation.trading.myorders.adapter

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.databinding.ItemMyOrdersBinding
import com.cryptoexchange.mobile.extensions.CURRENCY_LENGTH
import com.cryptoexchange.mobile.extensions.DD_MM_YYYY_HH_MM2
import com.cryptoexchange.source.entity.order.OrderModel
import com.cryptoexchange.source.entity.order.OrderSide
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class MyOrdersViewHolder(
    private val binding: ItemMyOrdersBinding,
    private val clickListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val buyTextColor = ContextCompat.getColor(itemView.context, R.color.puertoRico)
    private val buyBackgroundColor = ContextCompat.getColor(itemView.context, R.color.frostedMint)

    private val sellTextColor = ContextCompat.getColor(itemView.context, R.color.flamingo)
    private val sellBackgroundColor = ContextCompat.getColor(itemView.context, R.color.fairPink)

    private val dateFormat = SimpleDateFormat(DD_MM_YYYY_HH_MM2, Locale.getDefault())

    init {
        binding.closeOrderButton.setOnClickListener {
            if (adapterPosition != NO_POSITION) {
                clickListener.invoke(adapterPosition)
            }
        }
    }

    fun bind(order: OrderModel, currencyPair: String) {
        with(binding) {
            this.currencyPair.text = currencyPair
            orderId.text = order.id.toString()
            orderAmount.text = order
                .amount
                ?.stripTrailingZeros()
                ?.toPlainString() ?: "0"
            orderPrice.text = order
                .price
                ?.stripTrailingZeros()
                ?.toPlainString() ?: "0"
            orderStatus.text = getOrderStatus(order)
            orderTotal.text = getTotal(order)
            orderType.text = order.side?.name

            val textColor: Int
            val backgroundColor: Int
            if (order.side == OrderSide.BUY) {
                textColor = buyTextColor
                backgroundColor = buyBackgroundColor
            } else {
                textColor = sellTextColor
                backgroundColor = sellBackgroundColor
            }

            orderType.background = getStatusDrawable(backgroundColor, orderType.background)
            orderType.setTextColor(textColor)
            orderOpenTime.text = dateFormat.format(Date(order.createdAt ?: 0))
        }
    }

    private fun getTotal(order: OrderModel): String = order
        .side
        ?.getTotal(
            order.amount ?: BigDecimal.ONE,
            order.feeAmount ?: BigDecimal.ONE,
            order.price ?: BigDecimal.ONE
        )
        ?.stripTrailingZeros()
        ?.toPlainString() ?: "0"

    private fun getOrderStatus(order: OrderModel): String {
        val status = order
            .matchedAmount
            ?.divide(order.amount, RoundingMode.HALF_EVEN)
            ?.setScale(CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
            ?.stripTrailingZeros()
            ?.toPlainString()

        return String.format(STATUS_PATTERN, status)
    }

    private fun getStatusDrawable(color: Int, drawable: Drawable): Drawable {
        when (drawable) {
            is ShapeDrawable -> {
                drawable.paint.color = color
            }
            is GradientDrawable -> {
                drawable.setColor(color)
            }
            is ColorDrawable -> {
                drawable.color = color
            }
        }

        return drawable
    }

    companion object {
        private const val STATUS_PATTERN = "%s %%"
    }
}