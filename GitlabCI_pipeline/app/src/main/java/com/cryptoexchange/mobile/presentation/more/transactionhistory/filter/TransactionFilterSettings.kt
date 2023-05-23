package com.cryptoexchange.mobile.presentation.more.transactionhistory.filter

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.core.util.Pair
import com.cryptoexchange.mobile.domain.entity.filter.FilterDisplayValues
import com.cryptoexchange.mobile.extensions.DD_MM_YYYY
import com.cryptoexchange.mobile.extensions.FILTER_BACKEND_DATE_PATTERN
import com.cryptoexchange.mobile.extensions.toDateString
import com.cryptoexchange.mobile.extensions.toRange
import kotlinx.parcelize.Parcelize

/**
 * @param type - [com.cryptoexchange.source.entity.withdraw.Type]
 */
@Parcelize
data class TransactionFilterSettings(
    val dateFrom: Long?,
    val dateTo: Long?,
    val type: String?,
    val currency: String?
) : Parcelable {

    val rangeDate
        get() = Pair(dateFrom, dateTo).toRange("-", DD_MM_YYYY)

    val dateFromBackendFormatted: String?
        get() = dateFrom?.toDateString(FILTER_BACKEND_DATE_PATTERN)

    val dateToBackendFormatted: String?
        get() = dateTo?.plus(DateUtils.DAY_IN_MILLIS)?.toDateString(FILTER_BACKEND_DATE_PATTERN)

    val typeBackend: String?
        get() = type.takeIf {
            it != FilterDisplayValues.ALL && it != FilterDisplayValues.NOT_SELECTED
        }
}
