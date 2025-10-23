package com.tagomago.playlistmaker.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.tagomago.playlistmaker.Creator
import com.tagomago.playlistmaker.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences) : SettingsRepository {

    override fun getTheme(): Boolean {
        return sharedPrefs.getBoolean(MODE_NIGHTS, false)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {

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
    }
}