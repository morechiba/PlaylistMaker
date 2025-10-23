package com.tagomago.playlistmaker.domain.api

import com.tagomago.playlistmaker.domain.model.Track

interface TrackHistoryInteractor {
    fun saveTrack(track: Track)
    fun getTracks(): MutableList<Track>
    fun clearHistory()
}