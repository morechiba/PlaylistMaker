package com.tagomago.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<Button>(R.id.back)
        backButton.setOnClickListener() {
            finish()
        }

        val trackData = intent.getStringExtra(TRACK_DATA)
        val gson = Gson()
        val track: Track = gson.fromJson(trackData, Track::class.java)

        val trackName = findViewById<TextView>(R.id.trackName)
        val trackArtist = findViewById<TextView>(R.id.trackArtist)
        val trackTime = findViewById<TextView>(R.id.trackTime)
        val trackImage = findViewById<ImageView>(R.id.trackImage)
        val trackAlbum = findViewById<TextView>(R.id.trackAlbum)
        val trackAlbumLabel = findViewById<TextView>(R.id.trackAlbumLabel)
        val trackYear = findViewById<TextView>(R.id.trackYear)
        val trackYearLabel = findViewById<TextView>(R.id.trackYearLabel)
        val trackGenre = findViewById<TextView>(R.id.trackGenre)
        val trackCountry = findViewById<TextView>(R.id.trackCountry)

        fun getCoverUrl() = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

        trackName.text = track.trackName
        trackArtist.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country

        if(track.collectionName.isEmpty()){
            trackAlbum.visibility = View.GONE
            trackAlbumLabel.visibility = View.GONE
        } else {
            trackAlbum.text = track.collectionName
        }

        if(track.releaseDate.isEmpty()){
            trackYear.visibility = View.GONE
            trackYearLabel.visibility = View.GONE
        } else {
            trackYear.text = track.releaseDate.substring(0, 4)
        }

            Glide.with(this)
                .load(getCoverUrl())
                .centerCrop()
                .transform(RoundedCorners(8))
                .placeholder(R.drawable.placeholder_cover)
                .into(trackImage)

    }
}