package com.cryptoexchange.mobile.presentation.more.tradinghistory.adapter

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TradingAdapterScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val pageLoadListener: () -> Unit
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

        if (
            visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
            firstVisibleItemPosition >= 0 &&
            totalItemCount >= PAGE_SIZE
        ) {
            pageLoadListener.invoke()
        }
    }
}