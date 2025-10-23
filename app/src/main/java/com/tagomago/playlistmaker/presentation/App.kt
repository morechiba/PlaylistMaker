package com.tagomago.playlistmaker.presentation
import android.app.Application
import com.tagomago.playlistmaker.Creator

class App: Application() {

    var darkTheme = false
        // Only the class can modify the theme
        private set


    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)

        switchTheme(getSavedTheme())
    }

    fun getSavedTheme(): Boolean {
        val settingsInteractor = Creator.provideSettingsInteractor()
        darkTheme = settingsInteractor.getTheme()
        return darkTheme
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        val settingsInteractor = Creator.provideSettingsInteractor()
        settingsInteractor.switchTheme(darkThemeEnabled)
    }


    companion object{
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}