package com.neil.serviceexample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neil.serviceexample.ForegroundService.ServiceState
import com.neil.serviceexample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonStartService.setOnClickListener {
                Intent(this@MainActivity, ForegroundService::class.java)
                    .also {
                        it.putExtra(ForegroundService.SERVICE_STATE, ServiceState.STARTED.flag)
                        startService(it)
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