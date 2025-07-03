package com.tagomago.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tagomago.playlistmaker.App.Companion.PLAYLISTMAKER_PREFERENCES
import retrofit2.Retrofit
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private var editTextValue: String? = SEARCH_TEXT
    private lateinit var editText:EditText
    private lateinit var searchHint:TextView

    // В Kotlin для создания константной переменной мы используем companion object.
// Ключ должен быть константным, чтобы мы точно знали, что он не изменится
    companion object {
        const val SEARCH = "SEARCH"
        const val SEARCH_TEXT = ""
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesSearch = retrofit.create(ITunesApi::class.java)
    val trackList = mutableListOf<Track>()


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
        searchHint = findViewById<TextView>(R.id.searchHint)
        val placeholderNoFound = findViewById<LinearLayout>(R.id.placeholder_no_found)
        val placeholderNoConnection = findViewById<LinearLayout>(R.id.placeholder_error_connection)
        val searchHistoryBlock = findViewById<LinearLayout>(R.id.search_history)
        val SharedPrefs = getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)
        val searchHistory = SearchHistory(SharedPrefs)
        var trackListHistory = searchHistory.getTracks()
        val recyclerHistory = findViewById<RecyclerView>(R.id.trackListHistory)
        var adapterHistory = Adapter(trackListHistory)

        val backButton = findViewById<Button>(R.id.back)
        backButton.setOnClickListener {
            finish()
        }
        fun updateTrackListHistory() {
            trackListHistory = searchHistory.getTracks()
            trackListHistory.reverse()
            adapterHistory = Adapter(trackListHistory)
            recyclerHistory.adapter = adapterHistory
            recyclerHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            if(trackListHistory.size > 0) searchHistoryBlock.setVisibility(View.VISIBLE)
        }
        updateTrackListHistory()

        editText.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus && editText.text.isEmpty())
                {
                    searchHint.visibility = View.VISIBLE
                    searchHistoryBlock.visibility = View.GONE}
                else {
                    searchHint.visibility = View.GONE
                    updateTrackListHistory()
                }
            placeholderNoFound.setVisibility(View.GONE)
            placeholderNoConnection.setVisibility(View.GONE)
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchHint.visibility = if (editText.hasFocus() && p0?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        val searchHistoryClear = findViewById<Button>(R.id.search_history_button)
        searchHistoryClear.setOnClickListener{
            searchHistoryBlock.setVisibility(View.GONE)
            searchHistory.clearHistory()
            updateTrackListHistory()
        }

        val recycler = findViewById<RecyclerView>(R.id.trackList)
        val searchClear = findViewById<ImageView>(R.id.search_clear)
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


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchClear.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        editText.addTextChangedListener(simpleTextWatcher)

        val adapter = Adapter(trackList)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        fun searchSong() {
            trackList.clear()
            if (editText.text.isNotEmpty()) {
                placeholderNoFound.setVisibility(View.GONE)
                placeholderNoConnection.setVisibility(View.GONE)
                recycler.setVisibility(View.GONE)

                itunesSearch.search(editText.text.toString()).enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {

                        if (response.isSuccessful) {
                            val resultList = response.body()?.results
                            if (resultList?.isNotEmpty() == true) {
                                trackList.addAll(resultList!!)
                                adapter.notifyDataSetChanged()
                                if (trackList.isEmpty()) {
                                    placeholderNoFound.setVisibility(View.VISIBLE)
                                } else {
                                    recycler.setVisibility(View.VISIBLE)
                                }
                            } else {
                                placeholderNoFound.setVisibility(View.VISIBLE)
                            }

                        } else {
                            placeholderNoConnection.setVisibility(View.VISIBLE)
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        placeholderNoConnection.setVisibility(View.VISIBLE)

                    }

                })
            }
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchSong()
                }

            }
                false

        }

        val updateButton = findViewById<Button>(R.id.placeholder_button)
        updateButton.setOnClickListener {
            searchSong()
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

}
