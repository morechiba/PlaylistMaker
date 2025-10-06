package com.tagomago.playlistmaker

import com.tagomago.playlistmaker.data.TrackRepositoryImpl
import com.tagomago.playlistmaker.data.network.RetrofitNetworkClient
import com.tagomago.playlistmaker.domain.api.TrackInteractor
import com.tagomago.playlistmaker.domain.api.TrackRepository
import com.tagomago.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }
}