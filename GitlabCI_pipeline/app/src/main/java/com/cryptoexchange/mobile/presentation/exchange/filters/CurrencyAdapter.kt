package com.cryptoexchange.mobile.presentation.exchange.filters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.source.entity.currency.CurrencyResponse
import com.cryptoexchange.source.extensions.getCurrencyIcon
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.presentation.exchange.filters.CurrencyAdapter.CurrencyHolder

class CurrencyAdapter(
    private val currencies: List<CurrencyResponse>,
    private val onItemClicked: (CurrencyResponse) -> Unit
) :
    RecyclerView.Adapter<CurrencyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder =
        CurrencyHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_exchange_currency, parent, false)
        )

    override fun getItemCount(): Int = currencies.size

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        val item = currencies[position]
        holder.apply {
            title.text = item.shortName
            subTitle.text = item.fullName

            val currencyIcon =
                ContextCompat.getDrawable(itemView.context, getCurrencyIcon(item.shortName))
            icon.setImageDrawable(currencyIcon)

            itemView.setOnClickListener {
                onItemClicked.invoke(item)
            }
        }
    }

    class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: AppCompatTextView = itemView.findViewById(R.id.titleTextView)
        val icon: AppCompatImageView = itemView.findViewById(R.id.iconView)
        val subTitle: AppCompatTextView = itemView.findViewById(R.id.subTitleTextView)
    }
}
