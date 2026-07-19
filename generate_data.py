import os
from pathlib import Path

def write_file(path, content):
    p = Path(path)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(content, encoding='utf-8')

base = Path(r"c:\dcj1\app\src\main\java\com\cjack\ai")

# 1. Room Entity, DAO, DB
write_file(base / "data/local/entity/CprSessionEntity.kt", """package com.cjack.ai.data.local.entity

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
""")

write_file(base / "data/local/dao/CprSessionDao.kt", """package com.cjack.ai.data.local.dao

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
""")

write_file(base / "data/local/CjackDatabase.kt", """package com.cjack.ai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cjack.ai.data.local.dao.CprSessionDao
import com.cjack.ai.data.local.entity.CprSessionEntity

@Database(entities = [CprSessionEntity::class], version = 1, exportSchema = false)
abstract class CjackDatabase : RoomDatabase() {
    abstract val cprSessionDao: CprSessionDao
}
""")

# 2. Firebase Stub Layer
write_file(base / "data/remote/FirebaseService.kt", """package com.cjack.ai.data.remote

import com.cjack.ai.data.model.Telemetry
import javax.inject.Inject

class FirebaseService @Inject constructor() {
    fun uploadTelemetry(telemetry: Telemetry, aiStatus: String) {
        // Stub: Realtime Database upload once per second
        // database.reference.child("patients").child(patientId).setValue(...)
    }
}
""")

# 3. Main Data Repository
write_file(base / "data/repository/CjackRepository.kt", """package com.cjack.ai.data.repository

import com.cjack.ai.data.local.dao.CprSessionDao
import com.cjack.ai.data.model.Telemetry
import com.cjack.ai.data.remote.FirebaseService
import com.cjack.ai.data.remote.provider.ProviderFactory
import com.cjack.ai.data.remote.provider.TelemetryProvider
import com.cjack.ai.domain.engine.MotorCommand
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CjackRepository @Inject constructor(
    private val providerFactory: ProviderFactory,
    private val firebaseService: FirebaseService,
    private val cprSessionDao: CprSessionDao
) {
    private var currentProvider: TelemetryProvider = providerFactory.getProvider(isSimulationMode = true)
    
    fun setSimulationMode(enabled: Boolean) {
        currentProvider.disconnect()
        currentProvider = providerFactory.getProvider(enabled)
        currentProvider.connect()
    }
    
    fun observeTelemetry(): StateFlow<Telemetry> {
        return currentProvider.observeTelemetry()
    }
    
    fun connect() {
        currentProvider.connect()
    }
    
    fun disconnect() {
        currentProvider.disconnect()
    }

    fun sendMotorCommand(pwm: Int) {
        currentProvider.sendMotorCommand(pwm)
    }

    fun syncToFirebase(telemetry: Telemetry, aiStatus: String) {
        firebaseService.uploadTelemetry(telemetry, aiStatus)
    }
}
""")

# 4. UseCases
write_file(base / "domain/usecase/ObserveTelemetryUseCase.kt", """package com.cjack.ai.domain.usecase

import com.cjack.ai.data.model.Telemetry
import com.cjack.ai.data.repository.CjackRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveTelemetryUseCase @Inject constructor(
    private val repository: CjackRepository
) {
    operator fun invoke(): StateFlow<Telemetry> = repository.observeTelemetry()
}
""")

write_file(base / "domain/usecase/ProcessAiStatusUseCase.kt", """package com.cjack.ai.domain.usecase

import com.cjack.ai.data.model.Telemetry
import com.cjack.ai.domain.engine.EngineResult
import com.cjack.ai.domain.engine.RuleEngine
import javax.inject.Inject

class ProcessAiStatusUseCase @Inject constructor(
    private val ruleEngine: RuleEngine
) {
    operator fun invoke(telemetry: Telemetry): EngineResult {
        return ruleEngine.evaluate(telemetry)
    }
}
""")

print("Data & UseCases generated.")
