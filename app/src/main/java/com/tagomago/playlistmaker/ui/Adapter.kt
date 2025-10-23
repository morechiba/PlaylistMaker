package com.tagomago.playlistmaker.ui

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tagomago.playlistmaker.Creator
import com.tagomago.playlistmaker.presentation.App.Companion.CLICK_DEBOUNCE_DELAY
import com.tagomago.playlistmaker.R
import com.tagomago.playlistmaker.domain.model.Track

const val TRACK_DATA = "trackData"

class Adapter(private val tracks: MutableList<Track>) : RecyclerView.Adapter<ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                val track = tracks[position]

                val searchHistory = Creator.provideTrackHistoryInteractor()
                searchHistory.saveTrack(track)

                val displayIntent = Intent(holder.itemView.context, PlayerActivity::class.java)
                val gson = Gson()
                val trackData: String = gson.toJson(track)
                displayIntent.putExtra(TRACK_DATA, trackData)
                holder.itemView.context.startActivity(displayIntent)
            }
        }
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun getItemCount() = tracks.size
}