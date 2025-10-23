package com.tagomago.playlistmaker.domain.api

import com.tagomago.playlistmaker.domain.model.Track

interface TrackRepository {
    var resultCode: Int
    fun search(expression: String): List<Track>
}