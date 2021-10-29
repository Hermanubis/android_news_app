package edu.gwu.myapplication

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class sourceAdapter(val newsSources: List<sources>, val term: String) : RecyclerView.Adapter<sourceAdapter.ViewHolder>() {

    //get number of rows
    override fun getItemCount(): Int {
        return newsSources.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // A LayoutInflater is an object that knows how to read & parse an XML file
        val layoutInflater = LayoutInflater.from(parent.context)

        //new row creation at runtime, reference to the root layout
        val rootLayout: View = layoutInflater.inflate(R.layout.row_sources, parent, false)

        return ViewHolder(rootLayout)
    }

    // RecyclerView is ready to display a new (or recycled) row on the screen
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //row position/index are given.
        val currSource = newsSources[position]
        viewHolder.sourceName.text = currSource.sourceName
        viewHolder.sourceContent.text = currSource.sourceContent
        viewHolder.cardView.setOnClickListener {
            val intent: Intent = Intent(it.context, resultActivity::class.java)
            intent.putExtra("searchTerm", term)
            intent.putExtra("selectedSource", currSource.sourceName.toString())
            it.context.startActivity(intent)
        }
    }

    class ViewHolder(rootLayout: View) : RecyclerView.ViewHolder(rootLayout) {
        val sourceName: TextView = rootLayout.findViewById(R.id.sourceName)
        val sourceContent: TextView = rootLayout.findViewById(R.id.sourceContent)
        val cardView: CardView = rootLayout.findViewById(R.id.cardView)
    }
}