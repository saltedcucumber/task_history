package com.cryptoexchange.mobile.presentation.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.databinding.ItemDashboardCurrenciesBinding
import com.cryptoexchange.mobile.domain.entity.CurrencyHistory

class CurrenciesAdapter : RecyclerView.Adapter<CurrenciesViewHolder>() {

    private val currenciesRates = mutableListOf<CurrencyHistory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDashboardCurrenciesBinding.inflate(inflater, parent, false)

        return CurrenciesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int) {
        holder.bind(currenciesRates[position])
    }

    override fun getItemCount(): Int = currenciesRates.size

    fun setCurrenciesRates(currenciesRates: List<CurrencyHistory>) {
        this.currenciesRates.clear()
        this.currenciesRates.addAll(currenciesRates)

        notifyDataSetChanged()
    }
}