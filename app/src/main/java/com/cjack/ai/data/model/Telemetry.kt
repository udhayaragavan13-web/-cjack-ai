package com.cjack.ai.data.model

data class Telemetry(
    val heartRate: Int = 0,
    val spo2: Int = 0,
    val ecg: List<Float> = emptyList(),
    val pressure: Float = 0f,
    val compressionRate: Int = 0,
    val compressionDepth: Float = 0f,
    val temperature: Float = 0f,
    val battery: Int = 100,
    val signalStrength: Int = 100,
    val timestamp: Long = System.currentTimeMillis()
)
