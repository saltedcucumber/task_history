package com.cryptoexchange.mobile.presentation.more.activitylog.adapter

import androidx.recyclerview.widget.RecyclerView
import com.cryptoexchange.mobile.databinding.ItemActivityLogBinding
import com.cryptoexchange.mobile.extensions.DD_MM_YYYY_HH_MM2
import com.cryptoexchange.mobile.extensions.gone
import com.cryptoexchange.mobile.extensions.toDateString
import com.cryptoexchange.mobile.extensions.visible
import com.cryptoexchange.source.entity.user.activities.UserActivityModel
import java.util.*

class ActivityLogViewHolder(
    private val binding: ItemActivityLogBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(activity: UserActivityModel) {
        showDate(activity.time)
        with(binding) {
            activityLogData.text = toCamelCase(activity.type.name)
            activityLogCountry.text = activity.country
            activityLogCity.text = activity.city

            if (!activity.info?.isNullOrBlank()) {
                activityLogAdditionalData.text = activity.info
                activityLogAdditionalData.visible()
            } else {
                activityLogAdditionalData.gone()
            }
        }
    }

    private fun showDate(timeMills: Long) {
        val date = timeMills.toDateString(DD_MM_YYYY_HH_MM2)
        binding.activityLogDate.text = date
    }

    private fun toCamelCase(s: String): String {
        val parts = s.split("_").toTypedArray()
        var camelCaseString = ""
        for (part in parts) {
            camelCaseString = camelCaseString + " " + toProperCase(part)
        }
        return camelCaseString
    }

    private fun toProperCase(s: String): String {
        return s.substring(START_POSITION, END_POSITION).uppercase(Locale.getDefault()) +
            s.substring(END_POSITION).lowercase(Locale.getDefault())
    }

    companion object {
        private const val START_POSITION = 0
        private const val END_POSITION = 1
    }
}