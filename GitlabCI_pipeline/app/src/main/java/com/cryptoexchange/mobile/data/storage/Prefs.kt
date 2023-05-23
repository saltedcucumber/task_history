package com.cryptoexchange.mobile.data.storage

import android.content.Context
import javax.inject.Inject

class Prefs @Inject constructor(
    private val context: Context
) {

    private fun getSharedPreferences(prefsName: String) =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    //region app
    private val appPrefs by lazy { getSharedPreferences(APP_DATA) }

    var envName: String
        get() = appPrefs.getString(KEY_ENVIRONMENT_NAME, NO_ENVIRONMENT) ?: NO_ENVIRONMENT
        set(value) {
            appPrefs.edit().putString(KEY_ENVIRONMENT_NAME, value).apply()
        }

    var isTfaSuggested: Boolean
        get() = appPrefs.getBoolean(KEY_IS_TFA_SUGGESTED, false)
        set(value) {
            appPrefs.edit().putBoolean(KEY_IS_TFA_SUGGESTED, value).apply()
        }

    fun clear() {
        appPrefs.edit().clear().apply()
    }

    companion object {
        private const val APP_DATA = "app_data"

        private const val KEY_ENVIRONMENT_NAME = "KEY_ENVIRONMENT_NAME"
        private const val KEY_IS_TFA_SUGGESTED = "KEY_IS_TFA_SUGGESTED"

        private const val NO_ENVIRONMENT = "DEV_UNITEX"
    }
}
