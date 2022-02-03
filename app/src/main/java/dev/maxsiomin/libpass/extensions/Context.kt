package dev.maxsiomin.libpass.extensions

import android.content.Context
import androidx.preference.PreferenceManager
import dev.maxsiomin.libpass.util.SharedPrefs

fun Context.getDefaultSharedPrefs(): SharedPrefs =
    PreferenceManager.getDefaultSharedPreferences(this)

