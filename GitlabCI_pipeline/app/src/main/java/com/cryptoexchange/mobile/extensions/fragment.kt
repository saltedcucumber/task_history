package com.cryptoexchange.mobile.extensions

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import java.io.Serializable

fun Fragment.isAliveAndAvailable() = isAdded && !isRemoving && null != context && null != view

fun Fragment.navigate(
    @IdRes resId: Int,
    options: NavOptions,
    bundle: Bundle? = null
) {
    findNavController().navigate(resId, bundle, options)
}

fun Fragment.navigate(@IdRes resId: Int, bundle: Bundle? = null) {
    findNavController().navigate(resId, bundle)
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}

fun Fragment.popBackStack(@IdRes destinationId: Int, inclusive: Boolean = false) {
    findNavController().popBackStack(destinationId, inclusive)
}

fun Fragment.getNavHostFragment(@IdRes id: Int): NavHostFragment? {
    return childFragmentManager.findFragmentById(id) as? NavHostFragment
}

fun Fragment.addOnBackPressedCallback(callback: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                callback.invoke()
            }
        }
    )
}

fun Fragment.openEmailClient() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_APP_EMAIL)
    startActivity(Intent.createChooser(intent, ""))
}

fun Fragment.tryToGetString(
    key: String,
    exceptionMessage: String = composeErrorMessage(key)
): String {
    return this.arguments?.getString(key) ?: throwArgumentException(exceptionMessage)
}

fun Fragment.tryToGetInt(
    key: String,
    exceptionMessage: String = composeErrorMessage(key)
): Int {
    return this.arguments?.getInt(key) ?: throwArgumentException(exceptionMessage)
}

fun Fragment.tryToGetLong(
    key: String,
    exceptionMessage: String = composeErrorMessage(key)
): Long {
    return this.arguments?.getLong(key) ?: throwArgumentException(exceptionMessage)
}

fun Fragment.tryToGetBoolean(
    key: String,
    exceptionMessage: String = composeErrorMessage(key)
): Boolean {
    return this.arguments?.getBoolean(key) ?: throwArgumentException(exceptionMessage)
}

fun <T : Parcelable> Fragment.tryToGetParcelable(
    key: String,
    exceptionMessage: String = composeErrorMessage(key)
): T {
    return this.arguments?.getParcelable(key) ?: throwArgumentException(exceptionMessage)
}

fun <T : Parcelable> Fragment.tryToGetArrayList(
    key: String,
    exceptionMessage: String = composeErrorMessage(key)
): ArrayList<T> {
    return this.arguments?.getParcelableArrayList(key) ?: throwArgumentException(exceptionMessage)
}

fun <T : Serializable> Fragment.tryToGetSerializable(
    key: String,
    exceptionMessage: String = composeErrorMessage(key)
): T {
    return (this.arguments?.getSerializable(key) as? T) ?: throwArgumentException(exceptionMessage)
}

fun Fragment.getColor(@ColorRes id: Int) = ContextCompat.getColor(this.requireContext(), id)

fun Fragment.hideKeyboard() {
    val imm: InputMethodManager =
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view: View? = requireActivity().currentFocus
    if (view == null) {
        view = requireView()
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun <T> Fragment.observeBackStackSavedState(
    @IdRes fragmentId: Int,
    key: String,
    successBlock: (T?) -> Unit
) {
    val navController = findNavController()
    val navBackStackEntry = navController.getBackStackEntry(fragmentId)

    val observer = LifecycleEventObserver { _, event ->
        with(navBackStackEntry.savedStateHandle) {
            if (event == Lifecycle.Event.ON_RESUME &&
                contains(key)
            ) {
                val settings = get<T>(key)
                successBlock.invoke(settings)
                if (settings != null) {
                    remove<T>(key)
                }
            }
        }
    }
    navBackStackEntry.lifecycle.addObserver(observer)

    viewLifecycleOwner.lifecycle.addObserver(
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        }
    )
}

fun Fragment.getDrawable(@DrawableRes drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(requireContext(), drawableId)
}

private fun <T> throwArgumentException(message: String): T = throw IllegalArgumentException(message)

private fun composeErrorMessage(key: String) = "Argument $key hasn't been provided"