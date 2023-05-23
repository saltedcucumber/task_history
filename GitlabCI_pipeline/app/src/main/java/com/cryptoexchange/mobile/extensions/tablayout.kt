package com.cryptoexchange.mobile.extensions

import android.view.View
import com.google.android.material.tabs.TabLayout

fun TabLayout.newCustomTab(
    updatedView: View,
    gravity: Int
): TabLayout.Tab {
    return newTab().apply {
        customView = updatedView
        tabGravity = gravity
    }
}
