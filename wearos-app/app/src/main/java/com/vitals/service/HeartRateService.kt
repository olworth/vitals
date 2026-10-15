package com.vitals.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.health.services.client.HealthServices
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DeltaDataType
import androidx.health.services.client.unregisterMeasureCallback
import com.vitals.data.HeartRateState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeartRateService : Service() {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val measureClient by lazy { HealthServices.getClient(this).measureClient }

    private val callback = object : MeasureCallback {
        override fun onAvailabilityChanged(dataType: DeltaDataType<*, *>, availability: Availability) {}

        override fun onDataReceived(data: DataPointContainer) {
            val samples = data.getData(DataType.HEART_RATE_BPM)
            samples.lastOrNull()?.let { HeartRateState.update(it.value) }
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, buildNotification())
        measureClient.registerMeasureCallback(DataType.HEART_RATE_BPM, callback)
    }

    override fun onDestroy() {
        scope.launch {
            measureClient.unregisterMeasureCallback(DataType.HEART_RATE_BPM, callback)
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(): Notification {
        val channelId = "heart_rate_channel"
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(channelId, "Heart Rate Monitoring", NotificationManager.IMPORTANCE_LOW)
        )
        return Notification.Builder(this, channelId)
            .setContentTitle("Monitoring heart rate")
            .setSmallIcon(android.R.drawable.ic_menu_myplaces)
            .setOngoing(true)
            .build()
    }
}
