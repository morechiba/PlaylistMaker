package com.tagomago.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView
    private val trackArtist: TextView
    private val trackTime: TextView
    private val trackImage: ImageView

    init {
        trackName = itemView.findViewById(R.id.trackName)
        trackArtist = itemView.findViewById(R.id.trackArtist)
        trackTime = itemView.findViewById(R.id.trackTime)
        trackImage = itemView.findViewById(R.id.trackImage)
    }

    fun bind(model: Track) {
        trackName.text = model.trackName
        trackArtist.text = model.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(trackImage)

    }
}