package dev.maxsiomin.libpass

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    init {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun onCreate() {
        super.onCreate()

        // Light theme only
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    companion object {
        const val VERSION = "v${BuildConfig.VERSION_NAME}"
    }
}

const val APK_LOCATION = "https://maxsiomin.dev/apps/libpass/libpass.apk"

const val LINK_TO_GET_LIBPASS = "https://www.mos.ru/pgu/ru/services/link/5020/"
