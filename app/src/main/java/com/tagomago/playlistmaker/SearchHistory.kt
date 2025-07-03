package com.tagomago.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistory(val sharedPrefs: SharedPreferences) {
    companion object {
        const val SEARCH_HISTORY ="search_history"
    }

    val gson = Gson()

    fun getTracks(): MutableList<Track>{
        val searchHistory: String? = sharedPrefs.getString(SEARCH_HISTORY, "")
        var trackList: MutableList<Track> = mutableListOf<Track>()
        if(searchHistory?.isEmpty()!!){

        } else {
            val item = object : TypeToken<MutableList<Track>>() {}.type
            trackList = gson.fromJson(searchHistory, item)
        }
        return trackList
    }

    fun saveTrack(track: Track) {
        var searchHistory: String? = sharedPrefs.getString(SEARCH_HISTORY, "")

        var trackList: MutableList<Track> = gson.fromJson(searchHistory, object : TypeToken<MutableList<Track>>() {}.type) ?: mutableListOf<Track>()
        trackList.removeIf { it == track }
        if (trackList.size == 10) {
            trackList.removeAt(0)
        }
        trackList.add(track)
        searchHistory = gson.toJson(trackList)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY, searchHistory)
            .apply()
    }

    fun clearHistory() {
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY, "")
            .apply()
    }

}