package edu.gwu.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync

class sourceActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: sourceAdapter
    var category_ind = 0

    val categories = arrayOf<String>("Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sources)
        spinner = findViewById(R.id.spinner)
        //spinner.onItemSelectedListener = this

        // Retrieve data from the Intent that launched this screen
        val intent: Intent = getIntent()
        val term: String = intent.getStringExtra("TERM")!!

        val title = "Search for ${term}"
        setTitle(title)

        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this, android.R.layout.simple_spinner_item, categories)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.setSelection(category_ind)
        recyclerView = findViewById(R.id.recyclerView)
        val newsManager: newsManager = newsManager()
        val newsAPI = getString(R.string.newsAPI)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category_ind = position
                doAsync {
                    val newsSource: List<sources> = newsManager.retrieveSources(newsAPI, categories[category_ind])

                    adapter = sourceAdapter(newsSource)
                    runOnUiThread{
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@sourceActivity)
                    }
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


//        val newsSource: List<sources> = getFakeSources()
//
//
//         Sets scrolling direction to vertical
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        val adapter: sourceAdapter = sourceAdapter(newsSource)

    }

    fun getFakeSources(): List<sources> {
        return listOf(
            sources(
                sourceName = "BBC Global News",
                sourceContent = "BBC Global News is the home of the BBC’s award-winning international news, sport and features content, which reaches 121 million people around the world on TV, online, apps and social media each week."
            ),
            sources(
                sourceName = "NBC News",
                sourceContent = "Go to NBCNews.com for breaking news, videos, and the latest top stories in world news, business, politics, health and pop culture."
            ),
            sources(
                sourceName = "FOX News",
                sourceContent = "FOX News Channel (FNC) is a 24-hour all-encompassing news service dedicated to delivering breaking news as well as political and business news."
            ),
            sources(
                sourceName = "ESPN",
                sourceContent = "Visit ESPN to get up-to-the-minute sports news coverage, scores, highlights and commentary for NFL, MLB, NBA, College Football, NCAA Basketball and more."
            ),
            sources(
                sourceName = "Wall Street Journal",
                sourceContent = "WSJ online coverage of breaking news and current headlines from the US and around the world."
            )
        )
    }
}