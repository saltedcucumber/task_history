package com.cryptoexchange.mobile.presentation.trading.market.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.presentation.trading.market.adapter.CurrencyMarketAdapter.CurrencyMarketHolder
import com.cryptoexchange.source.entity.market.MarketModel

class CurrencyMarketAdapter(
    private val markets: List<MarketModel>,
    private var selectedPosition: Int,
    private val onItemClicked: (MarketModel, Int) -> Unit
) : RecyclerView.Adapter<CurrencyMarketHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyMarketHolder =
        CurrencyMarketHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_currency_market, parent, false)
        )

    override fun getItemCount(): Int = markets.size

    override fun onBindViewHolder(holder: CurrencyMarketHolder, position: Int) {
        val item = markets[position]
        holder.apply {
            currencyRadioButton.text = item.run {
                "$leftCurrencyCode - $rightCurrencyCode"
            }

            currencyRadioButton.isChecked = (position == selectedPosition)

            currencyRadioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onItemClicked.invoke(item, selectedPosition)
                    selectedPosition = adapterPosition
                }
            }
        }
    }

    class CurrencyMarketHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyRadioButton: RadioButton = itemView.findViewById(R.id.currencyMarketButton)
    }
}
