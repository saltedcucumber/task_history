package com.cryptoexchange.mobile.presentation.more.transactionhistory.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.R

class TransactionsHistoryDecorator : RecyclerView.ItemDecoration() {

    private var verticalMargin = 0
    private var horizontalMargin = 0

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (verticalMargin == 0) {
            with(parent.context) {
                verticalMargin = resources.getDimensionPixelOffset(R.dimen.history_vertical_margin)
                horizontalMargin = resources.getDimensionPixelOffset(R.dimen.horizontal_margin)
            }
        }

        with(outRect) {
            left = horizontalMargin
            right = horizontalMargin
            top = verticalMargin

            val position = parent.getChildAdapterPosition(view)
            val itemCount = state.itemCount

            if (position == itemCount) {
                bottom = verticalMargin
            }
        }
    }
}