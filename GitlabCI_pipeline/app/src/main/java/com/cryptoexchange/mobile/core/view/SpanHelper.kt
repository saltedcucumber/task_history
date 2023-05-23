package com.cryptoexchange.mobile.core.view

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.cryptoexchange.mobile.R

object SpanHelper {

    @JvmStatic
    fun getTermsPrivacyNote(
        context: Context,
        color: Int,
        onTermsClick: (() -> Unit)?,
        onPrivacyClick: (() -> Unit)?
    ): SpannableString {
        val string = context.getString(R.string.terms_conditions_text)
        val spannableString = SpannableString(string)
        val subStrTerms = context.getString(R.string.terms_conditions)
        val subStrPrivacy = context.getString(R.string.privacy_policy)

        addSpan(spannableString, subStrTerms, color, onTermsClick)
        addSpan(spannableString, subStrPrivacy, color, onPrivacyClick)

        return spannableString
    }

    @JvmStatic
    fun getAuthSuccessMessage(
        color: Int,
        email: String,
        context: Context,
        messageId: Int
    ): SpannableString {
        val message = context.getString(messageId)
        val messageFinal = String.format(message, email)
        val spannableString = SpannableString(messageFinal)

        addSpan(spannableString, email, color, null)

        return spannableString
    }

    private fun addSpan(
        spannableString: SpannableString,
        subStr: String,
        color: Int,
        onClick: (() -> Unit)?
    ) {
        val start = spannableString.toString().indexOf(subStr)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                onClick?.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = color
            }
        }
        spannableString.setSpan(
            clickableSpan,
            start,
            start + subStr.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}