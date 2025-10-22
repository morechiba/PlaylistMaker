package com.tagomago.playlistmaker.presentation
import android.app.Application
import com.tagomago.playlistmaker.Creator

class App: Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)
        val settingsInteractor = Creator.provideSettingsInteractor()
        val theme = settingsInteractor.getTheme()

        settingsInteractor.switchTheme(theme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        val settingsInteractor = Creator.provideSettingsInteractor()
        settingsInteractor.switchTheme(darkThemeEnabled)
    }


    companion object{
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}