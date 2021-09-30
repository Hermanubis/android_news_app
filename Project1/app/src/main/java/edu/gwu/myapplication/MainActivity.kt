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

class MainActivity : AppCompatActivity() {
    private lateinit var searchbar: SearchView
    private lateinit var search: Button
    private lateinit var mapButton: Button
    private lateinit var viewHeadline: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchbar = findViewById(R.id.searchbar)
        search = findViewById(R.id.search)
        mapButton = findViewById(R.id.mapButton)
        viewHeadline = findViewById(R.id.viewHeadline)

        search.isEnabled = false
    }
}