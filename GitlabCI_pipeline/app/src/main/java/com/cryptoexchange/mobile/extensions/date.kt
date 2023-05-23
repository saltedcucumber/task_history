package com.cryptoexchange.mobile.extensions

import androidx.core.util.component1
import androidx.core.util.component2
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val DD_MM_YYYY = "dd.MM.yyyy"
const val DD_MM_YYYY_HH_MM = "dd.MM.yyyy / HH:mm"
const val DD_MM_HH_MM = "dd.MM / HH:mm"
const val DD_MM_YYYY_HH_MM2 = "dd.MM.yyyy, HH:mm"
const val FILTER_BACKEND_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val YYYY_MM_DD = "yyyy-MM-dd"

const val THIRTY_DAYS = 30

fun Long.toDateString(pattern: String): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(Date(this))
}

fun String.fromRange(
    delimiter: String,
    pattern: String
): Pair<Long?, Long?> {
    val dates = this.split(delimiter)
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())

    val dateFrom = dates.firstOrNull()?.let {
        dateFormat.parseOrNull(it)?.time
    }
    val dateTo = dates.lastOrNull()?.let {
        dateFormat.parseOrNull(it)?.time
    }
    return Pair(dateFrom, dateTo)
}

fun SimpleDateFormat.parseOrNull(source: String): Date? = try {
    parse(source)
} catch (e: ParseException) {
    null
}

fun androidx.core.util.Pair<Long?, Long?>.toRange(
    delimiter: String,
    pattern: String
): String {
    val (startDateInMillis, endDateInMillis) = this

    if (startDateInMillis == null || endDateInMillis == null) {
        return ""
    }
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())

    val startDate = dateFormat.format(Date(startDateInMillis))
    val endDate = dateFormat.format(Date(endDateInMillis))

    return "$startDate $delimiter $endDate"
}
