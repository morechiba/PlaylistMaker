package com.tagomago.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<Button>(R.id.back)
        backButton.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(displayIntent)
        }

        val buttonPrivacy = findViewById<TextView>(R.id.privacy)

        buttonPrivacy.setOnClickListener {
            val displayIntent = Intent(Intent.ACTION_VIEW)
            displayIntent.data = Uri.parse("https://practicum.yandex.ru/")
        }
    }
}