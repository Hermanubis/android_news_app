package edu.gwu.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync

class resultActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: newsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        recyclerView = findViewById(R.id.result_recycler)

        // Retrieve data from the Intent that launched this screen
        val intent: Intent = getIntent()
        val searchTerm: String = intent.getStringExtra("searchTerm")!!
        val selectedSource: String = intent.getStringExtra("selectedSource")!!

        if(!searchTerm.isNullOrEmpty()) {

            val newsManager: newsManager = newsManager()
            val newsAPI = getString(R.string.newsAPI)

            if(selectedSource == "none") {
                val title = "Results for ${searchTerm}"
                setTitle(title)
                doAsync {

                    var articles: List<news> = newsManager.retrieveAllNews(newsAPI, searchTerm)
                    adapter = newsAdapter(articles)

                    runOnUiThread {
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@resultActivity)
                    }

                }
            }
            else{
                val title = "Results for ${searchTerm} from ${selectedSource}"
                setTitle(title)
                doAsync {

                    var articles: List<news> = newsManager.retrieveNewsFromSource(newsAPI, searchTerm, selectedSource)
                    adapter = newsAdapter(articles)

                    runOnUiThread {
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@resultActivity)
                    }

                }
            }
        }
        else{
            setTitle("No Search Term Found")
        }
    }
}
