package edu.gwu.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync
import kotlin.math.ceil

class headlineActivity : AppCompatActivity(){
    private lateinit var spinner: Spinner
    private lateinit var adapter: newsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var textView: TextView
    var category_ind = 0
    var page = 1
    var totalpage = 1

    val categories = arrayOf<String>("General", "Business", "Entertainment", "Health", "Science", "Sports", "Technology")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headline)
        spinner = findViewById(R.id.headline_spinner)
        prevButton = findViewById(R.id.prev_page)
        nextButton = findViewById(R.id.next_page)
        textView = findViewById(R.id.pageNum)

        // Retrieve data from the Intent that launched this screen

        val title = "Top Headlines"
        setTitle(title)

        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this, android.R.layout.simple_spinner_item, categories
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.setSelection(category_ind)
        recyclerView = findViewById(R.id.headline_recyclerView)
        val newsManager: newsManager = newsManager()
        val newsAPI = getString(R.string.newsAPI)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                category_ind = position
                doAsync {
                    val articles: List<news> = try {
                        newsManager.retrieveTopHeadline(newsAPI, categories[category_ind], "1")
                    } catch (exception: Exception) {
//                        Log.e("resultActivity", "Retrieving news failed!", exception)
                        listOf<news>()
                    }

                    runOnUiThread {
                        if (articles.isNotEmpty()) {
                            page = 1
                            totalpage = ceil(articles[0].totalResult.toDouble()/20.0).toInt()
                            prevButton.isEnabled = false
                            nextButton.isEnabled = totalpage > 1
                            textView.text = "${page} / ${totalpage}"
                            adapter = newsAdapter(articles)
                            recyclerView.adapter = adapter
                            page++
                            recyclerView.layoutManager = LinearLayoutManager(this@headlineActivity)
                        } else {
                            Toast.makeText(
                                this@headlineActivity,
                                getString(R.string.error_result),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        nextButton.setOnClickListener{
            if(page <= totalpage) {
                nextButton.isEnabled = true
                doAsync {
                    val articles: List<news> = try {
                        newsManager.retrieveTopHeadline(
                            newsAPI,
                            categories[category_ind],
                            page.toString()
                        )
                    } catch (exception: Exception) {
                        listOf<news>()
                    }

                    runOnUiThread {
                        if (articles.isNotEmpty()) {
                            prevButton.isEnabled = true
                            textView.text = "${page} / ${totalpage}"
                            if(page == totalpage){
                                nextButton.isEnabled = false
                            }
                            adapter = newsAdapter(articles)
                            recyclerView.adapter = adapter
                            page++
                            recyclerView.layoutManager = LinearLayoutManager(this@headlineActivity)
                        } else {
                            Toast.makeText(
                                this@headlineActivity,
                                getString(R.string.error_result),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

        prevButton.setOnClickListener{
            if(page > 1) {
                prevButton.isEnabled = true
                doAsync {
                    val articles: List<news> = try {
                        newsManager.retrieveTopHeadline(
                            newsAPI,
                            categories[category_ind],
                            page.toString()
                        )
                    } catch (exception: Exception) {
                        listOf<news>()
                    }

                    runOnUiThread {
                        if (articles.isNotEmpty()) {
                            nextButton.isEnabled = true
                            textView.text = "${page} / ${totalpage}"
                            if(page == 1){
                                prevButton.isEnabled = false
                            }
                            adapter = newsAdapter(articles)
                            recyclerView.adapter = adapter
                            page--
                            recyclerView.layoutManager = LinearLayoutManager(this@headlineActivity)
                        } else {
                            Toast.makeText(
                                this@headlineActivity,
                                getString(R.string.error_result),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

    }
}