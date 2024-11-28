package com.example.vettrack.utils

import android.content.Context
import timber.log.Timber

const val SHARED_PREFERENCES_NAME = "MyAppPrefs"
const val TOKEN_TAG = "FCM_TOKEN"

fun saveFCMToken(context: Context, token: String) {
    Timber.d("SharedPreferencesUtils_TAG: saveFCMToken: ")
    val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(TOKEN_TAG, token)
    editor.apply()
}

fun getFCMToken(context: Context): String? {
    Timber.d("SharedPreferencesUtils_TAG: getFCMToken: ")
    val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(TOKEN_TAG, null)
}