package com.vitals.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object HeartRateState {
    private val _latestHeartRate = MutableStateFlow<Double?>(null)
    val latestHeartRate: StateFlow<Double?> = _latestHeartRate

    fun update(value: Double) {
        _latestHeartRate.value = value
    }
}
