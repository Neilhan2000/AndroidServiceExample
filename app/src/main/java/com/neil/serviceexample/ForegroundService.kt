package com.neil.serviceexample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ForegroundService : Service() {

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
        when (ServiceState.from(flag = intent.getStringExtra(SERVICE_STATE))) {
            ServiceState.STARTED -> {
                val stringData = intent.getStringExtra(STRING_DATA)
                Log.d("Neil", "[NormalService] service text = $stringData")
            }
            ServiceState.STOPPED -> {
                stopSelf()
                Log.d("Neil", "[NormalService] Service stopped")
            }
        }
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
        const val SERVICE_STATE= "service state"
    }

    enum class ServiceState(val flag: String) {
        STARTED("start"), STOPPED("stop");
        companion object {
            fun from(flag: String?): ServiceState {
                return values().find { it.flag == flag } ?: throw NoSuchFieldException("state not exist")
            }
        }
    }
}