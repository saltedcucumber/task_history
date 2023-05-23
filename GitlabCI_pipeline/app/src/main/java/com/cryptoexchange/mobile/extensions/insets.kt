package com.cryptoexchange.mobile.extensions

import androidx.core.graphics.Insets
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.R
import android.view.WindowInsets
import androidx.core.view.WindowInsetsCompat

fun WindowInsets.getInsets(): Insets {
    val insetsCompat = WindowInsetsCompat.toWindowInsetsCompat(this)
    return when {
        SDK_INT >= R -> insetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
        else -> insetsCompat.systemWindowInsets
    }
}
