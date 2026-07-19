package com.cjack.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sos_logs")
data class SosLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val heartRate: Int,
    val spo2: Int,
    val compressionForce: Float,
    val compressionRate: Int,
    val latitude: Double,
    val longitude: Double,
    val aiSummary: String,
    val notificationStatus: String
)
