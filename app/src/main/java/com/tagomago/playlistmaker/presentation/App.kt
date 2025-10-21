package com.tagomago.playlistmaker.presentation
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.tagomago.playlistmaker.Creator
import androidx.core.content.edit

class App: Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)
        val sharedPrefs = Creator.provideSharedPreferences()

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
        val sharedPrefs = Creator.provideSharedPreferences()

        sharedPrefs.edit {
            putBoolean(MODE_NIGHTS, darkThemeEnabled)
        }

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object{
        const val MODE_NIGHTS = "mode_nights_yes"
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}