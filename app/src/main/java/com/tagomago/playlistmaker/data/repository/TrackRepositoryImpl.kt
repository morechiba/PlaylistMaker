package com.tagomago.playlistmaker.data.repository

import com.tagomago.playlistmaker.data.NetworkClient
import com.tagomago.playlistmaker.data.dto.TrackSearchRequest
import com.tagomago.playlistmaker.data.dto.TrackSearchResponse
import com.tagomago.playlistmaker.domain.api.TrackRepository
import com.tagomago.playlistmaker.domain.model.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {
    override var resultCode = 0
    override fun search(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        resultCode = response.resultCode
        if (resultCode == 200) {
            return (response as TrackSearchResponse).results.map {
                Track(
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.previewUrl,
                    it.trackId,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country
                )
            }
        } else {
            return emptyList()
        }
    }
}