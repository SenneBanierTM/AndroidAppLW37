package com.example.lw37automation

import android.app.Service
import android.content.Intent
import android.os.IBinder

class FloatingWidgetService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // TODO: Implement floating widget display logic
        return START_STICKY
    }

}
