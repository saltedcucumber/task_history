package com.cryptoexchange.mobile.presentation.more.transactionhistory.adapter

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.source.entity.withdraw.request.MoneyTransactionModel
import com.cryptoexchange.source.entity.withdraw.request.WithdrawStatus
import com.cryptoexchange.mobile.databinding.ItemTransactionHistoryBinding
import com.cryptoexchange.mobile.extensions.*
import com.cryptoexchange.source.extensions.getCurrencyIcon
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class TransactionHistoryViewHolder(
    private val binding: ItemTransactionHistoryBinding,
    private val onTransactionClicked: (withdrawId: Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(transaction: MoneyTransactionModel) {
        with(binding) {
            currencyIcon.setImageResource(getCurrencyIcon(transaction.currencyCode))
            currencyCode.text = transaction.currencyCode
            transactionIdValue.text = transaction.id.toString()
            transactionTypeValue.setText(getOperationType(transaction.type))

            showCurrencyValue(
                transactionAmountValue,
                transaction.amount,
                transaction.currencyCode
            )
            showCurrencyValue(
                transactionFeeValue,
                transaction.feeAmount,
                transaction.currencyCode
            )
            showTransactionStatus(transactionStatus, transaction.status)

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    when (transaction.status) {
                        WithdrawStatus.WAITING_FOR_EMAIL_CONFIRM -> onTransactionClicked.invoke(
                            transaction.id
                        )
                        else -> {}
                    }
                }
            }
        }

        showDate(transaction.createdAt)
    }

    private fun showCurrencyValue(view: TextView, value: BigDecimal, currencyCode: String) {
        val valueFormatted = value
            .setScale(CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
            .toPlainString()
        val valueStr = String.format(CURRENCY_PATTERN, valueFormatted, currencyCode)
        view.text = valueStr
    }

    private fun showTransactionStatus(statusView: TextView, status: WithdrawStatus) {
        val statusTextId = getOperationStatus(status)
        val statusTextColor = ContextCompat.getColor(itemView.context, getStatusTextColor(status))
        val statusBackgroundColor = getStatusBackgroundColor(status)

        statusView.setText(statusTextId)
        statusView.setTextColor(statusTextColor)
        statusView.background = getStatusDrawable(statusBackgroundColor, statusView.background)
    }

    private fun getStatusDrawable(@ColorRes colorId: Int, drawable: Drawable): Drawable {
        val color = ContextCompat.getColor(itemView.context, colorId)
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

    private fun showDate(timeMills: Long) {
        val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        val date = dateFormat.format(timeMills)
        binding.transactionDate.text = date
    }

    companion object {
        private const val CURRENCY_PATTERN = "%s %s"
        private const val DATE_PATTERN = "dd.MM.yyyy / HH:mm"
    }
}