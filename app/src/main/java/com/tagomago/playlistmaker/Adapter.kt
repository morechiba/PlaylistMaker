package com.tagomago.playlistmaker

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tagomago.playlistmaker.App.Companion.PLAYLISTMAKER_PREFERENCES

const val TRACK_DATA = "trackData"

class Adapter(private val tracks: MutableList<Track>) : RecyclerView.Adapter<ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            val track = tracks[position]
            val preferences = holder.itemView.context.getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)
            val searchHistory = SearchHistory(preferences)
            searchHistory.saveTrack(track)

            val displayIntent = Intent(holder.itemView.context, PlayerActivity::class.java)
            val gson = Gson()
            val trackData: String = gson.toJson(track)
            displayIntent.putExtra(TRACK_DATA, trackData)
            holder.itemView.context.startActivity(displayIntent)

        }
    }

    override fun getItemCount() = tracks.size
}