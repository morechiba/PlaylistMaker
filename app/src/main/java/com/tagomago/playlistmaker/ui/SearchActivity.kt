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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tagomago.playlistmaker.Creator
import com.tagomago.playlistmaker.R
import com.tagomago.playlistmaker.domain.api.TrackHistoryInteractor
import com.tagomago.playlistmaker.domain.api.TrackInteractor
import com.tagomago.playlistmaker.domain.model.Track

class SearchActivity : AppCompatActivity() {

    private var editTextValue: String? = SEARCH_TEXT
    private lateinit var editText:EditText
    private lateinit var placeholderNoFound: LinearLayout
    private lateinit var placeholderNoConnection: LinearLayout
    private lateinit var searchHistoryBlock: LinearLayout
    private lateinit var searchHistory: TrackHistoryInteractor
    private lateinit var adapterHistory: Adapter
    private lateinit var trackListHistory: MutableList<Track>
    private lateinit var recycler: RecyclerView
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var updateButton: Button
    private lateinit var progressBar: ProgressBar


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
        placeholderNoFound = findViewById<LinearLayout>(R.id.placeholder_no_found)
        placeholderNoConnection = findViewById<LinearLayout>(R.id.placeholder_error_connection)
        searchHistoryBlock = findViewById<LinearLayout>(R.id.search_history)
        recycler = findViewById<RecyclerView>(R.id.trackList)
        recyclerHistory = findViewById<RecyclerView>(R.id.trackListHistory)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        updateButton = findViewById<Button>(R.id.placeholder_button)
        searchHistory = Creator.provideTrackHistoryInteractor()
        trackListHistory = searchHistory.getTracks()

        val searchClearButton = findViewById<ImageView>(R.id.search_clear)
        val searchHistoryClearButton = findViewById<Button>(R.id.search_history_button)
        val backButton = findViewById<Button>(R.id.back)

        backButton.setOnClickListener {
            finish()
        }

        if(trackListHistory.size > 0) searchHistoryBlock.visibility = View.VISIBLE


        val adapter = Adapter(trackList)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val searchInteractor = Creator.provideTrackInteractor()

        val trackConsumer: TrackInteractor.TrackConsumer = object: TrackInteractor.TrackConsumer
        {
            @SuppressLint("NotifyDataSetChanged")
            override fun consume(foundTracks: List<Track>, resultCode: Int) {
                handler.post {
                    progressBar.visibility = View.GONE

                    if (resultCode != 0){
                        if (resultCode == 400) {
                            stateNoConnection()

                        } else if (foundTracks.isNotEmpty()) {
                            trackList.addAll(foundTracks)

                            recycler.visibility = View.VISIBLE
                            adapter.notifyDataSetChanged()

                            stateHideAll()
                        } else {
                            stateNoResults()
                        }
                    } else {
                        stateNoConnection()
                    }
                }
            }
        }

        editText.setOnFocusChangeListener { view, hasFocus ->
            stateHideAll()
        }

        searchHistoryClearButton.setOnClickListener{
            stateHideAll()
            searchHistory.clearHistory()
            updateTrackListHistory()
        }

        searchClearButton.setOnClickListener {
            editText.setText("")
            editText.clearFocus()
            recycler.visibility = View.GONE
            updateTrackListHistory()
            if(trackListHistory.size > 0) searchHistoryBlock.visibility = View.VISIBLE

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
            val searchExpression = editText.text
            if (searchExpression.isNotEmpty()) {
                stateProgressBarShow()
                searchInteractor.search(searchExpression.toString(), trackConsumer)
            } else {
                progressBar.visibility = View.GONE
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
            searchClearButton.visibility = clearButtonVisibility(text)
            searchDebounce()
        }

    }
    fun updateTrackListHistory() {
        placeholderNoFound.visibility = View.GONE
        placeholderNoConnection.visibility = View.GONE
        trackListHistory = searchHistory.getTracks()
        adapterHistory = Adapter(trackListHistory)
        recyclerHistory.adapter = adapterHistory
        recyclerHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    fun stateNoConnection() {
        placeholderNoConnection.visibility = View.VISIBLE
        placeholderNoFound.visibility = View.GONE
        progressBar.visibility = View.GONE
        searchHistoryBlock.visibility = View.GONE
    }
    fun stateNoResults() {
        placeholderNoFound.visibility = View.VISIBLE
        placeholderNoConnection.visibility = View.GONE
        progressBar.visibility = View.GONE
        searchHistoryBlock.visibility = View.GONE
    }

    fun stateProgressBarShow() {
        placeholderNoFound.visibility = View.GONE
        placeholderNoConnection.visibility = View.GONE
        recycler.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun stateHideAll() {
        placeholderNoFound.visibility = View.GONE
        placeholderNoConnection.visibility = View.GONE
        progressBar.visibility = View.GONE
        searchHistoryBlock.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        updateTrackListHistory()

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