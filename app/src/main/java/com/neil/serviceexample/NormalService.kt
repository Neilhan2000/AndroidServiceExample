package com.neil.serviceexample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class NormalService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("Neil", "[NormalService] Service created")
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Neil", "[NormalService] onStartCommand called")
        intent?.let { handleIntent(intent = it) }
        return START_STICKY
    }

    private fun handleIntent(intent: Intent) {
        val stringData = intent.getStringExtra(STRING_DATA)
        Log.d("Neil", "[NormalService] service text = $stringData")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Neil", "[NormalService] Service destroyed")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("Neil", "[NormalService] App swipe out")
    }

    companion object {
        const val STRING_DATA = "string data"
    }
}