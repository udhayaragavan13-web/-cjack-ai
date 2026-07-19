package com.cjack.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cpr_sessions")
data class CprSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Long,
    val durationMs: Long,
    val avgHeartRate: Int,
    val avgSpo2: Int,
    val outcome: String
)
