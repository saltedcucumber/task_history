package com.cryptoexchange.mobile.extensions

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.google.android.material.snackbar.Snackbar

val EditText.value: String
    get() = text.toString()

fun View.goneUnless(predicate: Boolean) = if (predicate) this.visible() else this.gone()

fun View.visibleUnless(predicate: Boolean) = if (predicate) this.gone() else this.visible()

fun View.invisibleUnless(predicate: Boolean) = if (predicate) this.visible() else this.invisible()

fun View.visible() = this.apply { this.visibility = View.VISIBLE }

fun View.invisible() = this.apply { this.visibility = View.INVISIBLE }

fun View.gone() = this.apply { this.visibility = View.GONE }

fun View.oppositeIsVisible() = isVisible == !isVisible

inline fun View.setIsVisibleBy(visibilityCondition: () -> Boolean) {
    isVisible = visibilityCondition.invoke()
}

fun Snackbar.setupAppSnackBar(): Snackbar.SnackbarLayout {
    val snackLayout = view as Snackbar.SnackbarLayout

    return snackLayout.apply {
        updateLayoutParams<FrameLayout.LayoutParams> {
            gravity = Gravity.TOP
        }
        setBackgroundColor(Color.TRANSPARENT)

        setOnApplyWindowInsetsListener { view, windowInsets ->
            windowInsets.also {
                view.updatePadding(top = it.getInsets().top)
            }
        }
    }
}

fun View.setDisablingClickListener(delay: Long = 500, perform: (View) -> Unit) {
    setOnClickListener {
        isEnabled = false
        perform(it)
        postDelayed(delay) {
            isEnabled = true
        }
    }
}

fun AutoCompleteTextView.setupDropdownMenu(
    items: List<String>,
    @LayoutRes layout: Int = android.R.layout.simple_selectable_list_item
) {
    setAdapter(
        ArrayAdapter(context, layout, items)
    )
}

fun TextView.clear() {
    text = null
}
