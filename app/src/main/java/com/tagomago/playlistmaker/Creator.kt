package com.tagomago.playlistmaker

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.tagomago.playlistmaker.data.repository.TrackRepositoryImpl
import com.tagomago.playlistmaker.data.network.RetrofitNetworkClient
import com.tagomago.playlistmaker.data.repository.SettingsRepositoryImpl
import com.tagomago.playlistmaker.data.storage.SharedPrefsStorageClient.Companion.PLAYLISTMAKER_PREFERENCES
import com.tagomago.playlistmaker.domain.api.SettingsInteractor
import com.tagomago.playlistmaker.domain.api.SettingsRepository
import com.tagomago.playlistmaker.domain.api.TrackHistoryInteractor
import com.tagomago.playlistmaker.domain.api.TrackInteractor
import com.tagomago.playlistmaker.domain.api.TrackRepository
import com.tagomago.playlistmaker.domain.impl.SettingsInteractorImpl
import com.tagomago.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.tagomago.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

    lateinit var application: Application

    fun initApplication (application: Application){
        this.application = application
    }

    private fun provideSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideTrackHistoryInteractor(): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(provideSharedPreferences())
    }

    private fun getSettingRepository(): SettingsRepository {
        return SettingsRepositoryImpl(provideSharedPreferences())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingRepository())
    }
}