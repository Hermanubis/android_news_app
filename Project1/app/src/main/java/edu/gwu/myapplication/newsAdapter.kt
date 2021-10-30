package edu.gwu.myapplication

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class newsAdapter(val news: List<news>) : RecyclerView.Adapter<newsAdapter.ViewHolder>() {

    //get number of rows
    override fun getItemCount(): Int {
        return news.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // A LayoutInflater is an object that knows how to read & parse an XML file
        val layoutInflater = LayoutInflater.from(parent.context)

        //new row creation at runtime, reference to the root layout
        val rootLayout: View = layoutInflater.inflate(R.layout.row_news, parent, false)

        return ViewHolder(rootLayout)
    }

    // RecyclerView is ready to display a new (or recycled) row on the screen
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //row position/index are given.
        val currNews = news[position]
        viewHolder.title.text = currNews.title
        viewHolder.content.text = currNews.content
        viewHolder.source.text = currNews.source
        if (currNews.iconurl.isNotBlank()) {
            //Picasso.get().setIndicatorsEnabled(true)

            Picasso
                .get()
                .load(currNews.iconurl)
                .into(viewHolder.thumbnail)
        }
        viewHolder.cardView.setOnClickListener {
            val intent: Intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(currNews.link)
            it.context.startActivity(intent)
        }
    }

    class ViewHolder(rootLayout: View) : RecyclerView.ViewHolder(rootLayout) {
        val title: TextView = rootLayout.findViewById(R.id.news_title)
        val content: TextView = rootLayout.findViewById(R.id.news_content)
        val thumbnail: ImageView = rootLayout.findViewById(R.id.newsIcon)
        val source: TextView = rootLayout.findViewById(R.id.news_source)
        val cardView: CardView = rootLayout.findViewById(R.id.newsCard)
    }
}