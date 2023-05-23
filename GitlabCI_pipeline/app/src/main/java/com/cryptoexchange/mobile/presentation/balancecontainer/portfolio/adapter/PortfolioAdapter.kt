package com.cryptoexchange.mobile.presentation.balancecontainer.portfolio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.databinding.ItemPortfolioBinding
import com.cryptoexchange.mobile.domain.entity.CurrencyInBasic
import java.math.BigDecimal

class PortfolioAdapter(
    private val currenciesInBasic: List<CurrencyInBasic>,
    private val totalBalance: BigDecimal
) : RecyclerView.Adapter<PortfolioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPortfolioBinding.inflate(inflater, parent, false)

        return PortfolioViewHolder(binding, totalBalance)
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        holder.bind(currenciesInBasic[position])
    }

    override fun getItemCount(): Int = currenciesInBasic.size
}
