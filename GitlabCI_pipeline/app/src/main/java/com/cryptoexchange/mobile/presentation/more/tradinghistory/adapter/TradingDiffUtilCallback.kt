package com.cryptoexchange.mobile.presentation.more.tradinghistory.adapter

import androidx.recyclerview.widget.DiffUtil
import com.cryptoexchange.mobile.domain.entity.TradingHistory

class TradingDiffUtilCallback : DiffUtil.ItemCallback<TradingHistory>() {

    override fun areItemsTheSame(oldItem: TradingHistory, newItem: TradingHistory): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: TradingHistory,
        newItem: TradingHistory
    ): Boolean = oldItem.tradeId == newItem.tradeId
}
