package com.cryptoexchange.mobile.core.global

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import javax.inject.Inject

class ResourceManager @Inject constructor(
    private val context: Context
) {

    fun getString(id: Int) = context.getString(id)

    fun getString(id: Int, vararg formatArgs: Any?): String {
        return context.getString(id).format(*formatArgs)
    }

    fun getErrorMessage(fieldStatus: Boolean, messageId: Int): String {
        return if (fieldStatus) {
            ""
        } else {
            getString(messageId)
        }
    }

    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(context, id)
    }

    fun saveToClipboard(text: String): Boolean {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText(text, text)
        clipboard?.setPrimaryClip(clip)

        return clipboard != null && clipboard.primaryClip?.description?.label.toString() == text
    }
}
