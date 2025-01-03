package com.example.lw37automation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewTags: RecyclerView
    private lateinit var editTextTag: EditText
    private lateinit var btnAddTag: Button
    private lateinit var btnRemoveTag: Button
    private lateinit var btnStart: Button
    private val tagsList = mutableListOf<String>()
    private lateinit var tagAdapter: TagAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewTags = findViewById(R.id.recyclerViewTags)
        editTextTag = findViewById(R.id.editTextTag)
        btnAddTag = findViewById(R.id.btnAddTag)
        btnRemoveTag = findViewById(R.id.btnRemoveTag)
        btnStart = findViewById(R.id.btnStart)

        // Initialize RecyclerView Adapter
        tagAdapter = TagAdapter(tagsList)
        recyclerViewTags.adapter = tagAdapter

        // Add Tag button logic
        btnAddTag.setOnClickListener {
            val tag = editTextTag.text.toString().trim()
            if (tag.isNotEmpty()) {
                tagsList.add(tag)
                tagAdapter.notifyItemInserted(tagsList.size - 1)
                editTextTag.text.clear()
                Toast.makeText(this, "Tag added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Enter a valid tag", Toast.LENGTH_SHORT).show()
            }
        }

        // Remove Tag button logic
        btnRemoveTag.setOnClickListener {
            val tag = editTextTag.text.toString().trim()
            if (tagsList.remove(tag)) {
                tagAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Tag removed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Tag not found", Toast.LENGTH_SHORT).show()
            }
        }

        // Start Automation button logic
        btnStart.setOnClickListener {
            val intent = Intent(this, FloatingWidgetService::class.java)
            startService(intent)
            Toast.makeText(this, "Automation Started", Toast.LENGTH_SHORT).show()
        }
    }
}
