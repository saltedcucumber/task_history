package com.cryptoexchange.mobile.presentation.more.activitylog.adapter

import androidx.recyclerview.widget.DiffUtil
import com.cryptoexchange.source.entity.user.activities.UserActivityModel

class ActivityLogDiffUtilCallback(
    private val oldList: List<UserActivityModel>,
    private val newList: List<UserActivityModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]

        return old == new
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]

        return old.id == new.id
    }
}