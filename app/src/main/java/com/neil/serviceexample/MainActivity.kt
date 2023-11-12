package com.neil.serviceexample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.neil.serviceexample.DownloadBoundService.ServiceState
import com.neil.serviceexample.DownloadBoundService.ServiceState.Companion
import com.neil.serviceexample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val serviceConnection = DownloadBoundServiceConnection(
        onServiceConnected = { service ->
            Log.d("Neil", "MainActivity${this.hashCode()} Connect to Service: $service")
        },
        onServiceDisconnected = {
            Log.d("Neil", "MainActivity${this.hashCode()} Disconnected")
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonBindService.setOnClickListener {
                Intent(this@MainActivity, DownloadBoundService::class.java)
                    .also {
                        bindService(
                            it,
                            serviceConnection,
                            Context.BIND_AUTO_CREATE
                        )
                    }
            }
            buttonStartService.setOnClickListener {
                Intent(this@MainActivity, DownloadBoundService::class.java)
                    .also {
                        it.putExtra(DownloadBoundService.SERVICE_STATE, ServiceState.STARTED.flag)
                        startService(it)
                    }
            }
            buttonUnbindService.setOnClickListener {
                Intent(this@MainActivity, DownloadBoundService::class.java)
                    .also {
                        try {
                            unbindService(serviceConnection)
                        } catch (e: java.lang.IllegalArgumentException) {
                            Log.d("Neil", "Service no registered")
                        }
                    }
            }
            buttonStopService.setOnClickListener {
                Intent(this@MainActivity, DownloadBoundService::class.java)
                    .also {
                        it.putExtra(DownloadBoundService.SERVICE_STATE, ServiceState.STOPPED.flag)
                        startService(it)
                    }
            }
        }
    }
}