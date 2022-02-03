package dev.maxsiomin.libpass.extensions

import androidx.preference.Preference

fun Preference.setOnClickListener(onClick: () -> Unit) {
    onPreferenceClickListener = Preference.OnPreferenceClickListener {
        onClick()
        true
    }
}
