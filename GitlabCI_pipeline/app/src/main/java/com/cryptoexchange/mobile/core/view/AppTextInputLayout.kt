package com.cryptoexchange.mobile.core.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.extensions.value

import com.google.android.material.textfield.TextInputLayout

class AppTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    @ColorRes
    var filledStrokeColor: Int = R.color.mineShaft

    @ColorRes
    var emptyStrokeColor: Int = R.color.doveGray

    override fun onFinishInflate() {
        super.onFinishInflate()

        editText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val text = editText?.value.toString()

                val color = if (text.isBlank()) {
                    val colorValue = ContextCompat.getColor(context, emptyStrokeColor)
                    boxStrokeColor = colorValue
                    defaultHintTextColor = ColorStateList.valueOf(colorValue)
                    colorValue
                } else {
                    val colorValue = ContextCompat.getColor(context, filledStrokeColor)
                    defaultHintTextColor = ColorStateList.valueOf(colorValue)
                    colorValue
                }

                val colorState = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_active),
                        intArrayOf(android.R.attr.state_focused),
                        intArrayOf(-android.R.attr.state_focused),
                        intArrayOf(android.R.attr.state_hovered),
                        intArrayOf(android.R.attr.state_enabled),
                        intArrayOf(-android.R.attr.state_enabled)
                    ),
                    intArrayOf(
                        color,
                        ContextCompat.getColor(context, R.color.eliteViolet),
                        color,
                        color,
                        color,
                        ContextCompat.getColor(context, emptyStrokeColor)
                    )
                )

                setBoxStrokeColorStateList(colorState)
            }
        }
    }
}