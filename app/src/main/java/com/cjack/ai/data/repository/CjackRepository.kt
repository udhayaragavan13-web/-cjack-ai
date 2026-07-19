package com.cjack.ai.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context,
    private val providerFactory: ProviderFactory,
    private val firebaseService: FirebaseService,
    private val cprSessionDao: CprSessionDao
) {
    private val prefs = context.getSharedPreferences("cjack_settings", Context.MODE_PRIVATE)
    private var currentProvider: TelemetryProvider

    init {
        val simMode = prefs.getBoolean("sensor_sim_mode", true)
        currentProvider = providerFactory.getProvider(isSimulationMode = simMode)
        currentProvider.connect()
    }
    
    fun setSimulationMode(enabled: Boolean) {
        currentProvider.disconnect()
        currentProvider = providerFactory.getProvider(enabled)
        currentProvider.connect()
    }
    
    fun observeTelemetry(): StateFlow<Telemetry> {
        return currentProvider.observeTelemetry()
    }
    
    fun connect() {
        val simMode = prefs.getBoolean("sensor_sim_mode", true)
        currentProvider.disconnect()
        currentProvider = providerFactory.getProvider(simMode)
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
