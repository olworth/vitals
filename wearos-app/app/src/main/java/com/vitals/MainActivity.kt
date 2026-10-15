package com.vitals

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vitals.service.HeartRateService
import com.vitals.ui.HeartRateScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HeartRateScreen() }
        startForegroundService(Intent(this, HeartRateService::class.java))
    }
}
