package com.cryptoexchange.mobile.core.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout

class CurrencyTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr), TextWatcher {

    var maxInputLength = 5000 // This is the default value for LengthFilter
    private val textBefore = StringBuilder("")

    override fun onFinishInflate() {
        super.onFinishInflate()

        editText?.addTextChangedListener(this)
    }

    override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {
        textBefore.append(string?.toString() ?: "")
    }

    override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
        if (string == null) {
            return
        }

        val text = string.toString()
        val textLength = string.length
        val textBeforeLength = textBefore.length
        when {
            text.length == 1 &&
                textBefore.isEmpty() &&
                (text.contains('0') || text.contains('.')) ->
                setCorrectValue(DECIMAL_START_VALUE)
            textLength == 1 && textBefore.contains(DECIMAL_START_VALUE) -> setCorrectValue("")
            textLength > maxInputLength &&
                textBeforeLength < textLength &&
                count <= 1 -> setCorrectValue(textBefore.toString())
            textLength == maxInputLength + 1 && textBeforeLength < textLength ->
                setCorrectValue(textBefore.toString())
        }
    }

    override fun afterTextChanged(s: Editable?) {
        textBefore.clear()
    }

    private fun setCorrectValue(value: String) {
        editText?.apply {
            removeTextChangedListener(this@CurrencyTextInputLayout)
            setText(value)
            setSelection(length())
            addTextChangedListener(this@CurrencyTextInputLayout)
        }
    }

    companion object {
        private const val DECIMAL_START_VALUE = "0."
    }
}