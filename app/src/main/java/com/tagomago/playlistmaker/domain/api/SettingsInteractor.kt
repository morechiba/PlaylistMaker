package com.tagomago.playlistmaker.domain.api

interface SettingsInteractor {
    fun getTheme(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
}