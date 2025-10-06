package com.tagomago.playlistmaker.domain.api

import com.tagomago.playlistmaker.domain.model.Track

interface TrackInteractor {
    fun search(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>)
    }
}