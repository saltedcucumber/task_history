package com.cryptoexchange.mobile.presentation.more.activitylog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.databinding.ItemActivityLogBinding
import com.cryptoexchange.source.entity.user.activities.UserActivityModel

class ActivityLogAdapter : RecyclerView.Adapter<ActivityLogViewHolder>() {

    private val activities = mutableListOf<UserActivityModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityLogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemActivityLogBinding.inflate(inflater, parent, false)

        return ActivityLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityLogViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int = activities.size

    fun setItems(activities: List<UserActivityModel>) {
        val diffCallback = ActivityLogDiffUtilCallback(this.activities, activities)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.activities.clear()
        this.activities.addAll(activities)

        diffResult.dispatchUpdatesTo(this)
    }
}