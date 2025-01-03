package com.example.lw37automation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TagAdapter(private val tags: List<String>) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.tagTextView.text = tags[position]
    }

    override fun getItemCount(): Int = tags.size

    class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagTextView: TextView = itemView.findViewById(android.R.id.text1)
    }
}
