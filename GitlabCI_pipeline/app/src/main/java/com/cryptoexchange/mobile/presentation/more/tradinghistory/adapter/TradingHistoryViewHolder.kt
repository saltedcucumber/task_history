package com.cryptoexchange.mobile.presentation.more.tradinghistory.adapter

import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.databinding.ItemTradingHistoryBinding
import com.cryptoexchange.mobile.domain.entity.TradingHistory
import com.cryptoexchange.mobile.extensions.CURRENCY_LENGTH
import com.cryptoexchange.mobile.extensions.DD_MM_YYYY_HH_MM
import com.cryptoexchange.mobile.extensions.toDateString
import com.cryptoexchange.mobile.extensions.toDisplayStringBy
import com.cryptoexchange.source.entity.order.OrderSide
import com.cryptoexchange.source.extensions.getCurrencyIcon
import java.math.RoundingMode

class TradingHistoryViewHolder(
    private val binding: ItemTradingHistoryBinding,
    private val detailsClickListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.tradeDetails.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                detailsClickListener.invoke(adapterPosition)
            }
        }
    }

    fun bind(trading: TradingHistory) {
        with(binding) {
            leftCurrencyIcon.setImageResource(getCurrencyIcon(trading.leftCurrencyCode))
            leftCurrencyName.text = trading.leftCurrencyCode
            rightCurrencyIcon.setImageResource(getCurrencyIcon(trading.rightCurrencyCode))
            rightCurrencyName.text = trading.rightCurrencyCode

            val leftStr: String
            val rightStr: String
            val leftCurrencyCode: String
            val rightCurrencyCode: String
            val total: String
            trading.apply {
                total = side.getTotal(amount, fee, price)?.toDisplayStringBy().toString()
            }
            val tradeTypeIconId = if (trading.side == OrderSide.BUY) {
                leftCurrencyCode = trading.leftCurrencyCode
                rightCurrencyCode = trading.rightCurrencyCode
                leftStr = total

                rightStr = trading
                    .received
                    .setScale(CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
                    .toPlainString()
                R.drawable.ic_arrow_buy
            } else {
                rightCurrencyCode = trading.leftCurrencyCode
                leftCurrencyCode = trading.rightCurrencyCode
                leftStr = total

                rightStr = trading
                    .amount
                    .setScale(CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
                    .toPlainString()
                R.drawable.ic_arrow_sell
            }
            tradeTypeIcon.setImageResource(tradeTypeIconId)

            val amount = String.format(
                AMOUNT_CURRENCY_PATTERN,
                leftStr,
                leftCurrencyCode
            )
            amountCurrency.text = amount

            val received = String.format(
                RECEIVED_CURRENCY_PATTERN,
                rightStr,
                rightCurrencyCode
            )
            receivedCurrency.text = received
        }

        showDate(trading.date)
    }

    private fun showDate(timeMills: Long) {
        binding.tradeDate.text = timeMills.toDateString(DD_MM_YYYY_HH_MM)
    }

    companion object {
        private const val AMOUNT_CURRENCY_PATTERN = "%s %s /"
        private const val RECEIVED_CURRENCY_PATTERN = " %s %s"
    }
}