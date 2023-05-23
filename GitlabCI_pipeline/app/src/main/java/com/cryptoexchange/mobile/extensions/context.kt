package com.cryptoexchange.mobile.extensions

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat.checkSelfPermission
import kotlin.math.roundToInt

fun Context.dpToPx(dp: Float) =
    (dp * (resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()

fun Context.requirePermissions(
    permissions: Array<String>,
    performIfAllIsGranted: () -> Unit,
    performIfSomeDenied: (Array<String>) -> Unit = {},
) {
    val deniedPermissionIds =
        permissions.filter { (checkSelfPermission(this, it) == PERMISSION_DENIED) }

    if (deniedPermissionIds.isEmpty()) {
        performIfAllIsGranted()
    } else {
        performIfSomeDenied(deniedPermissionIds.toTypedArray())
    }
}

fun Context.requirePermission(
    permission: String,
    performIfGranted: () -> Unit,
    performIfDenied: (String) -> Unit = {},
) {
    val denied = checkSelfPermission(this, permission) == PERMISSION_DENIED

    if (!denied) {
        performIfGranted()
    } else {
        performIfDenied(permission)
    }
}
