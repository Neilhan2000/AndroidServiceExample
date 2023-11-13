package com.neil.serviceexample

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.neil.serviceexample.ForegroundService.ServiceState
import com.neil.serviceexample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

        binding.apply {
            buttonStartService.setOnClickListener {
                Intent(this@MainActivity, ForegroundService::class.java)
                    .also {
                        it.putExtra(ForegroundService.SERVICE_STATE, ServiceState.STARTED.flag)
                        startForegroundService(it)
                    }
            }
            buttonStopService.setOnClickListener {
                Intent(this@MainActivity, ForegroundService::class.java)
                    .also {
                        it.putExtra(ForegroundService.SERVICE_STATE, ServiceState.STOPPED.flag)
                        startService(it)
                    }
            }
        }
    }
}