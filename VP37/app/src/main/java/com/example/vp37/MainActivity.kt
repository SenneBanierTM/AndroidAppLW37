package com.example.vp37

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vp37.databinding.ActivityMainBinding
import com.example.vp37.databinding.DialogAddTagBinding
import com.google.android.material.snackbar.Snackbar
import android.provider.Settings
import android.net.Uri

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tagList = mutableListOf<String>()
    private lateinit var tagAdapter: TagAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        tagAdapter = TagAdapter(tagList) { tag ->
            tagList.remove(tag)
            tagAdapter.notifyDataSetChanged()
            Snackbar.make(binding.recyclerViewTags, getString(R.string.tag_removed_message, tag), Snackbar.LENGTH_SHORT).show()
        }
        binding.recyclerViewTags.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTags.adapter = tagAdapter

        // Handle Add Button
        binding.btnAdd.setOnClickListener {
            showAddTagDialog()
        }

        // Handle Floating UI Button with permission check
        binding.btnFloatingUI.setOnClickListener {
            checkOverlayPermission()
        }
    }

    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 101) // 101 is a request code
        } else {
            startFloatingService()
        }
    }

    private fun startFloatingService() {
        val intent = Intent(this, FloatingUIService::class.java)
        startService(intent)
        finish() // Close the app
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (Settings.canDrawOverlays(this)) {
                startFloatingService()
            } else {
                Snackbar.make(binding.root, getString(R.string.overlay_permission_request), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun showAddTagDialog() {
        val dialogBinding = DialogAddTagBinding.inflate(LayoutInflater.from(this))
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.add_tag_title))
            .setView(dialogBinding.root)
            .setPositiveButton(getString(R.string.add_button)) { _, _ ->
                val tag = dialogBinding.etTagInput.text.toString()
                if (tag.isNotBlank()) {
                    tagList.add(tag)
                    tagAdapter.notifyDataSetChanged()
                } else {
                    Snackbar.make(binding.recyclerViewTags, getString(R.string.tag_empty_error), Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.cancel_button), null)
            .show()
    }
}