package com.tagomago.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import retrofit2.Retrofit
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private var editTextValue: String? = SEARCH_TEXT
    private lateinit var editText:EditText


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

        val backButton = findViewById<Button>(R.id.back)
        backButton.setOnClickListener {
            finish()
        }

        val searchClear = findViewById<ImageView>(R.id.search_clear)
        searchClear.setOnClickListener {
            editText.setText("")
            editText.clearFocus()

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

        val recycler = findViewById<RecyclerView>(R.id.trackList)

        val trackList = ArrayList<Track>()
        val adapter = Adapter(trackList)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {



                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (editText.text.isNotEmpty()) {
                        itunesSearch.search(editText.text.toString()).enqueue(object : Callback<SearchResponse> {
                            override fun onResponse(
                                call: Call<SearchResponse>,
                                response: Response<SearchResponse>
                            ) {
                                if (response.code() == 200) {
                                    trackList.clear()
                                    if (response.body()?.results?.isNotEmpty() == true) {
                                        trackList.addAll(response.body()?.results!!)
                                        adapter.notifyDataSetChanged()
                                    }
                                    //   if (tracks.isEmpty()) {
                                    //        showMessage(getString(R.string.nothing_found), "")
                                    //   } else {
                                    //        showMessage("", "")
                                    //    }
                                } else {
                                    //  showMessage(getString(R.string.something_went_wrong), response.code().toString())
                                }
                            }

                            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                                //    showMessage(getString(R.string.something_went_wrong), t.message.toString())
                            }

                        })
                    }
                }


                true
            }
                false

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
