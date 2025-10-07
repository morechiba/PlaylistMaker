package com.tagomago.playlistmaker.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tagomago.playlistmaker.App.Companion.PLAYLISTMAKER_PREFERENCES
import com.tagomago.playlistmaker.Creator
import com.tagomago.playlistmaker.R
import com.tagomago.playlistmaker.domain.SearchHistory
import com.tagomago.playlistmaker.domain.api.TrackInteractor
import com.tagomago.playlistmaker.domain.model.Track

class SearchActivity : AppCompatActivity() {

    private var editTextValue: String? = SEARCH_TEXT
    private lateinit var editText:EditText
    private lateinit var searchHint:TextView

    val trackList = mutableListOf<Track>()

    private var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editText = findViewById<EditText>(R.id.search)
        val placeholderNoFound = findViewById<LinearLayout>(R.id.placeholder_no_found)
        val placeholderNoConnection = findViewById<LinearLayout>(R.id.placeholder_error_connection)
        val searchHistoryBlock = findViewById<LinearLayout>(R.id.search_history)
        val sharedPrefs = getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPrefs)
        var trackListHistory = searchHistory.getTracks()
        val recyclerHistory = findViewById<RecyclerView>(R.id.trackListHistory)
        var adapterHistory = Adapter(trackListHistory)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val updateButton = findViewById<Button>(R.id.placeholder_button)

        val backButton = findViewById<Button>(R.id.back)
        backButton.setOnClickListener {
            finish()
        }

        val recycler = findViewById<RecyclerView>(R.id.trackList)
        val searchClear = findViewById<ImageView>(R.id.search_clear)

        val adapter = Adapter(trackList)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val searchInteractor = Creator.provideTrackInteractor()

        val trackConsumer: TrackInteractor.TrackConsumer = object: TrackInteractor.TrackConsumer
        {
            @SuppressLint("NotifyDataSetChanged")
            override fun consume(foundTracks: List<Track>, resultCode: Int) {
                handler.post {
                    progressBar.setVisibility(View.GONE)
                    if (resultCode != 0){
                        if (resultCode == 400) {
                            placeholderNoConnection.setVisibility(View.VISIBLE)
                            progressBar.setVisibility(View.GONE)

                        } else if (foundTracks.isNotEmpty()) {
                            trackList.addAll(foundTracks)
                            recycler.setVisibility(View.VISIBLE)
                            adapter.notifyDataSetChanged()
                            placeholderNoFound.setVisibility(View.GONE)
                            placeholderNoConnection.setVisibility(View.GONE)
                        } else {

                            placeholderNoFound.setVisibility(View.VISIBLE)
                            placeholderNoConnection.setVisibility(View.GONE)
                        }

                    } else {
                        placeholderNoConnection.setVisibility(View.VISIBLE)
                        progressBar.setVisibility(View.GONE)
                    }




                }

            }
        }

        fun updateTrackListHistory() {
            placeholderNoFound.setVisibility(View.GONE)
            placeholderNoConnection.setVisibility(View.GONE)
            trackListHistory = searchHistory.getTracks()
            adapterHistory = Adapter(trackListHistory)
            recyclerHistory.adapter = adapterHistory
            recyclerHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            if(trackListHistory.size > 0) {
                searchHistoryBlock.setVisibility(View.VISIBLE)
            }
        }
        updateTrackListHistory()

        editText.setOnFocusChangeListener { view, hasFocus ->
            placeholderNoFound.setVisibility(View.GONE)
            placeholderNoConnection.setVisibility(View.GONE)
        }

        val searchHistoryClear = findViewById<Button>(R.id.search_history_button)
        searchHistoryClear.setOnClickListener{
            searchHistoryBlock.setVisibility(View.GONE)
            searchHistory.clearHistory()
            updateTrackListHistory()
        }

        searchClear.setOnClickListener {
            editText.setText("")
            editText.clearFocus()
            recycler.setVisibility(View.GONE)
            updateTrackListHistory()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        }

        fun clearButtonVisibility(s: CharSequence?): Int {
            return if (s.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        fun searchSong() {
            trackList.clear()
            if (editText.text.isNotEmpty()) {
                placeholderNoFound.setVisibility(View.GONE)
                placeholderNoConnection.setVisibility(View.GONE)
                searchHistoryBlock.setVisibility(View.GONE)
                recycler.setVisibility(View.GONE)
                progressBar.setVisibility(View.VISIBLE)

                searchInteractor.search(editText.text.toString(), trackConsumer)
            } else {
                progressBar.setVisibility(View.GONE)
            }
        }

        updateButton.setOnClickListener {
            searchSong()
        }

        val searchRunnable = Runnable { searchSong() }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        editText.doOnTextChanged { text, _, _, _ ->
            searchClear.visibility = clearButtonVisibility(text)
            searchDebounce()
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        editTextValue = editText.getText().toString()
        outState.putString(SEARCH, editTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editText.setText(savedInstanceState.getString(SEARCH))
    }

    // В Kotlin для создания константной переменной мы используем companion object.
// Ключ должен быть константным, чтобы мы точно знали, что он не изменится
    companion object {
        const val SEARCH = "SEARCH"
        const val SEARCH_TEXT = ""
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}