package com.tagomago.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView) {

    private val trackName: TextView
    private val trackArtist: TextView
    private val trackTime: TextView
    private val trackImage: TextView

    init {
        trackName = parentView.findViewById(R.id.trackName)
        trackArtist = parentView.findViewById(R.id.trackArtist)
        trackTime = parentView.findViewById(R.id.trackTime)
        trackImage = parentView.findViewById(R.id.trackImage)
    }

    fun bind(model: Track) {
        trackName.text = model.trackName
        trackArtist.text = model.artistName
        trackTime.text = model.trackTime
        trackImage.text = model.artworkUrl100
    }
}