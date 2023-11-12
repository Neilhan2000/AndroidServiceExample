package com.neil.serviceexample

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class ForegroundService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val notificationBuilder = NotificationCompat.Builder(this, FOREGROUND_DOWNLOAD_CHANNEL_ID)

    override fun onCreate() {
        super.onCreate()
        Log.d("Neil", "[NormalService] Service created")
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Neil", "[NormalService] onStartCommand called")
        intent?.let { handleIntent(intent = it) }
        return START_NOT_STICKY
    }

    private fun handleIntent(intent: Intent) {
        when (ServiceState.from(flag = intent.getStringExtra(SERVICE_STATE))) {
            ServiceState.STARTED -> {
                startDownloadInForeground()
                Log.d("Neil", "[NormalService] Download service started")
            }
            ServiceState.STOPPED -> {
                stopForeground(Service.STOP_FOREGROUND_DETACH)
                stopSelf()
                Log.d("Neil", "[NormalService] Download service stopped")
            }
        }
    }

    private fun startDownloadInForeground() {
        val notification = getDownloadNotification()
        startForeground(NOTIFICATION_ID, notification.build())
        startDownload()
    }

    private fun startDownload() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Note: Other scopes will be launched if we click startButton multiple times.
        serviceScope.launch {
            (1..10).forEach { downloadProgress ->
                delay(1000)
                notificationManager.notify(NOTIFICATION_ID, updatedDownloadNotification(progress = downloadProgress).build())
            }
        }
    }

    private fun getDownloadNotification() = notificationBuilder
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setOnlyAlertOnce(true)
        .setContentTitle("File Downloading")
        .setContentText("progress 0/10")
        .addAction(
            R.drawable.baseline_stop_24,
            "Stop",
            PendingIntent.getService(
                this,
                0,
                Intent(this, ForegroundService::class.java).apply { putExtra(SERVICE_STATE, ServiceState.STOPPED.flag) },
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        )

    private fun updatedDownloadNotification(progress: Int) = notificationBuilder
        .setContentText("progress $progress/10")

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        Log.d("Neil", "[NormalService] Service destroyed")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("Neil", "[NormalService] App swipe out")
    }

    companion object {
        const val SERVICE_STATE= "service state"
        const val NOTIFICATION_ID = 1
        const val FOREGROUND_DOWNLOAD_CHANNEL_ID = "foreground download channel id"
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