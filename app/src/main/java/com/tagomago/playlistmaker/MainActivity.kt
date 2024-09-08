package com.tagomago.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.search)

        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на кнопку SEARCH", Toast.LENGTH_SHORT).show()
            }
        }

        buttonSearch.setOnClickListener(imageClickListener)

        val buttonMedia = findViewById<Button>(R.id.media)

        buttonMedia.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку MEDIA LIBRARY", Toast.LENGTH_SHORT).show()
        }

        val buttonSetting = findViewById<Button>(R.id.setting)

        buttonSetting.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку SETTINGS", Toast.LENGTH_SHORT).show()
        }

    }


}