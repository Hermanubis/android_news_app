package edu.gwu.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var searchbar: SearchView
    private lateinit var search: Button
    private lateinit var mapButton: Button
    private lateinit var viewHeadline: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences: SharedPreferences = getSharedPreferences("android-news", Context.MODE_PRIVATE)

        searchbar = findViewById(R.id.searchbar)
        search = findViewById(R.id.search)
        mapButton = findViewById(R.id.mapButton)
        viewHeadline = findViewById(R.id.viewHeadline)

        search.isEnabled = false

        val savedSearch = preferences.getString("SEARCHTERM", "")
        val enableButton: Boolean = savedSearch!!.isNotBlank()
        search.isEnabled = enableButton
        searchbar.setQuery(savedSearch, false)

        searchbar.setOnSearchClickListener{
            val editor = preferences.edit()
            editor.putString("SEARCHTERM", searchbar.query.toString()).apply()
            val intent: Intent = Intent(this, sourceActivity::class.java)
            intent.putExtra("TERM", searchbar.query.toString())
            startActivity(intent)
        }


        searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(term: String?): Boolean {
                val editor = preferences.edit()
                editor.putString("SEARCHTERM", term).apply()
                val intent: Intent = Intent(applicationContext, sourceActivity::class.java)
                intent.putExtra("TERM", searchbar.query.toString())
                startActivity(intent)
                return false
            }

            override fun onQueryTextChange(term1: String?): Boolean {
                val editor = preferences.edit()
                editor.putString("SEARCHTERM", term1).apply()
                val enableButton: Boolean = searchbar.query.isNotBlank()
                search.isEnabled = enableButton
                return enableButton
            }
        })

        search.setOnClickListener {
            val editor = preferences.edit()
            editor.putString("SEARCHTERM", searchbar.query.toString()).apply()
            val intent: Intent = Intent(this, sourceActivity::class.java)
            intent.putExtra("TERM", searchbar.query.toString())
            startActivity(intent)
        }

        mapButton.setOnClickListener{
            val intent: Intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        viewHeadline.setOnClickListener{
            val intent: Intent = Intent(this, headlineActivity::class.java)
            startActivity(intent)
        }
    }

//    private val textWatcher: TextWatcher = object : TextWatcher {
//        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            // Kotlin shorthand for username.getText().toString()
//            // .toString() is needed because getText() returns an Editable (basically a char array).
//            val inputtedSearch: String = searchbar.query.toString()
//            val enableButton: Boolean = inputtedSearch.isNotBlank()
//
//            // Kotlin shorthand for login.setEnabled(enableButton)
//            search.isEnabled = enableButton
//        }
//
//        override fun afterTextChanged(p0: Editable?) {}
//    }
}