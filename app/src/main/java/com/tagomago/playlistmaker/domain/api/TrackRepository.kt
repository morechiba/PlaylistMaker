package com.tagomago.playlistmaker.domain.api

import com.tagomago.playlistmaker.domain.model.Track

interface TrackRepository {
    fun search(expression: String): List<Track>
}