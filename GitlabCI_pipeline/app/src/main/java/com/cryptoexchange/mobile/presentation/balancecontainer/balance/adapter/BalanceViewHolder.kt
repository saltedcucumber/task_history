package com.cryptoexchange.mobile.presentation.balancecontainer.balance.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.cryptoexchange.source.entity.wallet.WalletModel
import com.cryptoexchange.mobile.databinding.ItemBalanceBinding
import com.cryptoexchange.mobile.extensions.CURRENCY_LENGTH
import com.cryptoexchange.source.extensions.getCurrencyIcon
import com.cryptoexchange.source.extensions.getCurrencyName
import java.math.RoundingMode

class BalanceViewHolder(
    private val binding: ItemBalanceBinding,
    private val depositClickListener: (Int) -> Unit,
    private val withdrawClickListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        with(binding) {
            depositButton.setOnClickListener {
                if (adapterPosition != NO_POSITION) {
                    depositClickListener.invoke(adapterPosition)
                }
            }
            withdrawButton.setOnClickListener {
                if (adapterPosition != NO_POSITION) {
                    withdrawClickListener.invoke(adapterPosition)
                }
            }
        }
    }

    fun bind(walletModel: WalletModel) {
        with(binding) {
            coinValue.text = walletModel.currencyShortName
            nameValue.text = getCurrencyName(itemView.context, walletModel.currencyShortName)
            currencyIcon.setImageResource(getCurrencyIcon(walletModel.currencyShortName))

            val available =
                walletModel.isAvailable.setScale(CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
            val reserved = walletModel.reserved.setScale(CURRENCY_LENGTH, RoundingMode.HALF_EVEN)
            val total = walletModel.getTotal().setScale(CURRENCY_LENGTH, RoundingMode.HALF_EVEN)

            availableBalanceValue.text = available.toPlainString()
            reservedTitleValue.text = reserved.toPlainString()
            totalBalanceValue.text = total.toPlainString()
        }
    }
}