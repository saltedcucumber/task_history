package com.cryptoexchange.mobile.domain.entity.filter

import com.cryptoexchange.mobile.core.enumextension.NamesTransporter

data class FilterDisplayValues(
    private val values: NamesTransporter
) {
    private val enumValues = values.names

    val displayValues: List<String>
        get() = listOf(ALL, *enumValues)

    companion object {

        fun createFromList(listValues: List<String>): FilterDisplayValues {
            return FilterDisplayValues(object : NamesTransporter {
                override val names: Array<String>
                    get() = listValues.toTypedArray()
            })
        }

        const val ALL = "ALL"
        const val NOT_SELECTED = ""
    }
}
