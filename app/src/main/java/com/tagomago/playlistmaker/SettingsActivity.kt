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
            finish()
        }

        val buttonShare = findViewById<TextView>(R.id.share)

        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            val message = getString(R.string.share_link)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        val buttonSupport = findViewById<TextView>(R.id.support)

        buttonSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            val subject = getString(R.string.support_mail_subject)
            val message = getString(R.string.support_mail_message)
            val mail = getString(R.string.support_mail)
            supportIntent.data = Uri.parse("mailto:");
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            supportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(supportIntent)
        }

        val buttonPrivacy = findViewById<TextView>(R.id.privacy)

        buttonPrivacy.setOnClickListener {
            val link = getString(R.string.privacy_link)
            val displayIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(displayIntent)
        }
    }
}