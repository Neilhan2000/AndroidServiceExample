package com.neil.serviceexample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neil.serviceexample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonStartService.setOnClickListener {
                Intent(this@MainActivity, NormalService::class.java)
                    .also {
                        it.putExtra(NormalService.STRING_DATA, editTextService.text.toString())
                        it.putExtra(NormalService.SERVICE_STATE, NormalService.ServiceState.STARTED.flag)
                        startService(it)
                    }
            }
            buttonStopService.setOnClickListener {
//                Intent(this@MainActivity, NormalService::class.java)
//                    .also {
//                        it.putExtra(NormalService.SERVICE_STATE, NormalService.ServiceState.STOPPED.flag)
//                        startService(it)
//                    }
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}