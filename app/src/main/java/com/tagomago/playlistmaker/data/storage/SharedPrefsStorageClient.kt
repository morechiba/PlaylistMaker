package com.tagomago.playlistmaker.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.tagomago.playlistmaker.data.StorageClient
import java.lang.reflect.Type
import androidx.core.content.edit

class SharedPrefsStorageClient <T>(
    private val context: Context,
    private val dataKey: String,
    private val type: Type
    ): StorageClient<T> {

        private val sharedPrefs: SharedPreferences = context.getSharedPreferences(PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE)
        private val gson = Gson()

        override fun saveData(data: T) {
            sharedPrefs.edit { putString(dataKey, gson.toJson(data, type)).apply() }
        }

        override fun getData(): T? {
            val dataJson = sharedPrefs.getString(dataKey, null)
            return if (dataJson == null) {
                null
            } else {
                gson.fromJson(dataJson, type)
            }
        }

        override fun clearData() {
            sharedPrefs.edit { remove(dataKey).apply() }
        }


    companion object{
        const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"
    }
}