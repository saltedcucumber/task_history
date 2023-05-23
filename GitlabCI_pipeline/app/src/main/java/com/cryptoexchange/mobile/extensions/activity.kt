package com.cryptoexchange.mobile.extensions

import androidx.fragment.app.FragmentActivity

fun FragmentActivity.restart(supportTransition: Boolean = false) {
    if (supportTransition) {
        supportFinishAfterTransition()
    } else {
        finish()
    }
    startActivity(intent)
}
