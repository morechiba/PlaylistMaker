package com.tagomago.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


class App: Application() {
         var darkTheme = false

        override fun onCreate() {
            super.onCreate()
            val sharedPrefs = getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)
            darkTheme = sharedPrefs.getBoolean(MODE_NIGHTS, false)

            AppCompatDelegate.setDefaultNightMode(
                if (darkTheme) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )

        }

        fun switchTheme(darkThemeEnabled: Boolean) {
            darkTheme = darkThemeEnabled
            val sharedPrefs = getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)
            sharedPrefs.edit()
                .putBoolean(MODE_NIGHTS, darkThemeEnabled)
                .apply()

            AppCompatDelegate.setDefaultNightMode(
                if (darkThemeEnabled) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
    companion object{
        const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"
        const val MODE_NIGHTS = "mode_nights_yes"
    }
}