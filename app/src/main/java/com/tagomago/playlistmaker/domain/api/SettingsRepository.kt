package com.tagomago.playlistmaker.domain.api

interface SettingsRepository {
    fun getTheme(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
}