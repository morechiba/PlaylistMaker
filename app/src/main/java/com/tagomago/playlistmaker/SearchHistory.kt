package com.tagomago.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistory(val sharedPrefs: SharedPreferences) {
    companion object {
        const val SEARCH_HISTORY ="search_history"
    }

    val gson = Gson()

    fun searchT() {
        val searchHistory = sharedPrefs.getString(SEARCH_HISTORY, "")

        if(searchHistory?.isEmpty()!!){
            sharedPrefs.edit()
                .putString(SEARCH_HISTORY, saveTracks())
                .apply()
        }
    }



    fun getTracks(): MutableList<Track>{
        val searchHistory: String? = sharedPrefs.getString(SEARCH_HISTORY, "")
        var trackList: MutableList<Track> = mutableListOf<Track>()
        if(searchHistory?.isEmpty()!!){

        } else {
            val track = object : TypeToken<MutableList<Track>>() {}.type
            trackList = gson.fromJson(searchHistory, track)
        }
        return trackList
    }

    fun saveTracks(): String {
        val trackList = gson.toJson(getTracks())
        return trackList
    }

    fun clearHistory() {
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY, "")
            .apply()
    }

}