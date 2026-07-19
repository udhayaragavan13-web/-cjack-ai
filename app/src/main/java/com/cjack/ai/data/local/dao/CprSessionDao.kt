package com.cjack.ai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cjack.ai.data.local.entity.CprSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CprSessionDao {
    @Insert
    suspend fun insertSession(session: CprSessionEntity)

    @Query("SELECT * FROM cpr_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<CprSessionEntity>>
}
