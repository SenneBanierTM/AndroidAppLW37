package com.example.vp37

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast

class FloatingUIService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var regularFloatingView: View
    private lateinit var settingsFloatingView: View

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Show the first (regular) floating UI
        showRegularFloatingUI()
    }

    private fun showRegularFloatingUI() {
        // Inflate the regular floating UI layout
        regularFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_ui, null)

        // Configure layout parameters
        val params = createLayoutParams()

        // Add the floating view to the window
        windowManager.addView(regularFloatingView, params)

        // Set up button listeners
        regularFloatingView.findViewById<ImageButton>(R.id.btnStart).setOnClickListener {
            Toast.makeText(this, getString(R.string.start), Toast.LENGTH_SHORT).show()
        }

        regularFloatingView.findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            // Transition to settings floating UI
            windowManager.removeView(regularFloatingView)
            showSettingsFloatingUI()
        }

        regularFloatingView.findViewById<ImageButton>(R.id.btnStop).setOnClickListener {
            stopSelf() // Stop the service
        }

        // Enable dragging
        enableDragging(regularFloatingView, params)
    }

    private fun showSettingsFloatingUI() {
        // Inflate the settings floating UI layout
        settingsFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_settings_floating_ui, null)

        // Configure layout parameters
        val params = createLayoutParams()

        // Add the settings floating view to the window
        windowManager.addView(settingsFloatingView, params)

        // Set up button listeners
        settingsFloatingView.findViewById<ImageButton>(R.id.btnPlus).setOnClickListener {
            Toast.makeText(this, getString(R.string.plus), Toast.LENGTH_SHORT).show()
        }

        settingsFloatingView.findViewById<ImageButton>(R.id.btnMinus).setOnClickListener {
            Toast.makeText(this, getString(R.string.minus), Toast.LENGTH_SHORT).show()
        }

        settingsFloatingView.findViewById<ImageButton>(R.id.btnCurve).setOnClickListener {
            Toast.makeText(this, getString(R.string.curve), Toast.LENGTH_SHORT).show()
        }

        // Handle returning to the regular floating UI
        settingsFloatingView.findViewById<ImageButton>(R.id.btnReturn).setOnClickListener {
            windowManager.removeView(settingsFloatingView)
            showRegularFloatingUI()
        }

        // Enable dragging
        enableDragging(settingsFloatingView, params)
    }

    private fun createLayoutParams(): WindowManager.LayoutParams {
        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    }

    private fun enableDragging(view: View, params: WindowManager.LayoutParams) {
        view.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX + (event.rawX - initialTouchX).toInt()
                        params.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager.updateViewLayout(view, params)
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::regularFloatingView.isInitialized) {
            windowManager.removeView(regularFloatingView)
        }
        if (::settingsFloatingView.isInitialized) {
            windowManager.removeView(settingsFloatingView)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
