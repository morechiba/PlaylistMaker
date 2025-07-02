package com.tagomago.playlistmaker

import android.content.Context.MODE_PRIVATE
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.tagomago.playlistmaker.App.Companion.PLAYLISTMAKER_PREFERENCES

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
        }
    }

    override fun getItemCount() = tracks.size
}