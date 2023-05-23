package com.cryptoexchange.mobile.presentation.exchange

import com.google.android.material.tabs.TabLayout

class OnTransactionTypeTabSelectedListener(
    private val onTabSelected: (transactionType: TransactionType) -> Unit
) : TabLayout.OnTabSelectedListener {

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val type = tab?.tag as? TransactionType ?: return
        onTabSelected.invoke(type)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        /* no-op */
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        /* no-op */
    }
}
