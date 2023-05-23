package com.cryptoexchange.mobile.presentation.trading.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.presentation.trading.chart.ChartFragment
import com.cryptoexchange.mobile.presentation.trading.myorders.MyOrdersFragment
import com.cryptoexchange.mobile.presentation.trading.orders.OrdersFragment
import com.cryptoexchange.mobile.presentation.trading.ordershistory.OrdersHistoryFragment

class TradingPagerAdapter(
    private val context: Context,
    manager: FragmentManager
) : FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = listOf(
        ChartFragment(),
        OrdersFragment(),
        OrdersHistoryFragment(),
        MyOrdersFragment()
    )

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence =
        when (position) {
            0 -> getString(R.string.chart)
            1 -> getString(R.string.orders)
            2 -> getString(R.string.orders_history)
            3 -> getString(R.string.my_orders)
            else -> throw IllegalArgumentException("Unknown tab position")
        }

    private fun getString(@StringRes stringId: Int) = context.getString(stringId)
}