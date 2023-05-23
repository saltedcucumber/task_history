package com.cryptoexchange.mobile.presentation.balancecontainer

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.BalanceFragment
import com.cryptoexchange.mobile.presentation.balancecontainer.portfolio.PortfolioFragment

class BalancePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments = listOf(BalanceFragment(), PortfolioFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}