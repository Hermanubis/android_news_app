package edu.gwu.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class sourceAdapter(val newsSources: List<sources>) : RecyclerView.Adapter<sourceAdapter.ViewHolder>() {

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
    }

    class ViewHolder(rootLayout: View) : RecyclerView.ViewHolder(rootLayout) {
        val sourceName: TextView = rootLayout.findViewById(R.id.sourceName)
        val sourceContent: TextView = rootLayout.findViewById(R.id.sourceContent)
    }
}