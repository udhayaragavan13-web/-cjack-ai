package com.cjack.ai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cjack.ai.data.local.entity.SosLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SosLogDao {
    @Insert
    suspend fun insertSosLog(log: SosLogEntity)

    @Query("SELECT * FROM sos_logs ORDER BY timestamp DESC")
    fun getAllSosLogs(): Flow<List<SosLogEntity>>
}
