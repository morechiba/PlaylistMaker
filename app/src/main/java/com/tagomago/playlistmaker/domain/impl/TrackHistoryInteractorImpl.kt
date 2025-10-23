package com.tagomago.playlistmaker.domain.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tagomago.playlistmaker.domain.api.TrackHistoryInteractor
import com.tagomago.playlistmaker.domain.model.Track

class TrackHistoryInteractorImpl(private val sharedPrefs: SharedPreferences): TrackHistoryInteractor {

    private val gson = Gson()

    override fun saveTrack(track: Track) {
        var searchHistory: String? = sharedPrefs.getString(SEARCH_HISTORY, "")
        val item = object : TypeToken<MutableList<Track>>() {}.type
        val trackList: MutableList<Track> = gson.fromJson(searchHistory, item) ?: mutableListOf<Track>()
        trackList.removeIf { it == track }
        if (trackList.size == 10) {
            trackList.removeAt(9)
        }
        trackList.add(0, track)
        searchHistory = gson.toJson(trackList)
        sharedPrefs.edit {
            putString(SEARCH_HISTORY, searchHistory).apply()
        }
    }

    override fun getTracks(): MutableList<Track> {
        val searchHistory: String? = sharedPrefs.getString(SEARCH_HISTORY, "")
        var trackList: MutableList<Track> = mutableListOf<Track>()
        if(searchHistory.isNullOrEmpty()){

        } else {
            val item = object : TypeToken<MutableList<Track>>() {}.type
            trackList = gson.fromJson(searchHistory, item)
        }
        return trackList
    }

    override  fun clearHistory() {
        sharedPrefs.edit {
            putString(SEARCH_HISTORY, "").apply()
        }
    }

    companion object {
        const val SEARCH_HISTORY ="search_history"
    }
}