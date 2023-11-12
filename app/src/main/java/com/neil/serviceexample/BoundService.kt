package com.neil.serviceexample

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class DownloadBoundService() : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val totalFileCount = 10
    private var downloadProgress = 0
    private var isDownloadStarted = false

    override fun onCreate() {
        super.onCreate()
        Log.d("Neil", "[BoundService] onCreated called")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Neil", "[BoundService] onStartCommand called")
        intent?.let { handleIntent(it) }
        return START_STICKY
    }

    private fun handleIntent(intent: Intent) {
        when (ServiceState.from(flag = intent.getStringExtra(SERVICE_STATE))) {
            ServiceState.STARTED -> {
                val stringData = intent.getStringExtra(STRING_DATA)
                Log.d("Neil", "[BoundService] service text = $stringData")
            }
            ServiceState.STOPPED -> {
                stopSelf()
                Log.d("Neil", "[BoundService] Service stopped")
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("Neil", "[BoundService] onBind called")
        return DownloadBinder()
    }


    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("Neil", "[BoundService] onUnbind called")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
        Log.d("Neil", "[BoundService] onDestroy called")
    }

    fun startDownload() {
        if (!isDownloadStarted) {
            isDownloadStarted = true

            serviceScope.launch {
                (1..totalFileCount).forEach {
                    delay(6000)
                    downloadProgress += 1
                }
                isDownloadStarted = false
            }
        }
    }

    fun getDownloadProgress() = DownloadProgress(
        total = totalFileCount,
        currentProgress = downloadProgress
    )

    inner class DownloadBinder() : Binder() {
        fun getService(): DownloadBoundService = this@DownloadBoundService
    }

    enum class ServiceState(val flag: String) {
        STARTED("start"), STOPPED("stop");
        companion object {
            fun from(flag: String?): ServiceState {
                return values().find { it.flag == flag } ?: throw NoSuchFieldException("state not exist")
            }
        }
    }

    data class DownloadProgress(
        val total: Int,
        val currentProgress: Int
    )

    companion object {
        const val SERVICE_STATE = "service_state"
        const val STRING_DATA = "string_data"
    }
}

class DownloadBoundServiceConnection(
    private val onServiceConnected: (service: DownloadBoundService) -> Unit,
    private val onServiceDisconnected: () -> Unit
): ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val serviceInstance = (service as DownloadBoundService.DownloadBinder).getService()
        onServiceConnected.invoke(serviceInstance)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        onServiceDisconnected.invoke()
    }
}