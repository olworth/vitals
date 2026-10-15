package com.vitals.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.vitals.data.HeartRateState

@Composable
fun HeartRateScreen() {
    val heartRate by HeartRateState.latestHeartRate.collectAsState()
    Text("\n\n\n\n\n\nHeart rate: $heartRate")
}
