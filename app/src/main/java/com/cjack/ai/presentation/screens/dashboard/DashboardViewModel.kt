package com.cjack.ai.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjack.ai.data.local.dao.SosLogDao
import com.cjack.ai.data.local.entity.SosLogEntity
import com.cjack.ai.data.model.Telemetry
import com.cjack.ai.data.remote.HelpHubClient
import com.cjack.ai.data.repository.CjackRepository
import com.cjack.ai.domain.engine.AiStatus
import com.cjack.ai.domain.engine.EngineResult
import com.cjack.ai.domain.engine.MotorCommand
import com.cjack.ai.domain.engine.VoiceQueueManager
import com.cjack.ai.domain.usecase.ObserveTelemetryUseCase
import com.cjack.ai.domain.usecase.ProcessAiStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

val DEFAULT_SAFE_TELEMETRY = Telemetry(
    heartRate = 72,
    spo2 = 98,
    ecg = List(50) { 0f },
    pressure = 0f,
    compressionRate = 0,
    compressionDepth = 0f,
    temperature = 36.5f,
    battery = 100,
    signalStrength = 4,
    timestamp = System.currentTimeMillis()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val observeTelemetryUseCase: ObserveTelemetryUseCase,
    private val processAiStatusUseCase: ProcessAiStatusUseCase,
    private val repository: CjackRepository,
    private val voiceManager: VoiceQueueManager,
    private val helpHubClient: HelpHubClient,
    private val sosLogDao: SosLogDao
) : ViewModel() {

    private val _telemetry = MutableStateFlow(Telemetry())
    val telemetry: StateFlow<Telemetry> = _telemetry.asStateFlow()

    private val _aiResult = MutableStateFlow(
        EngineResult(AiStatus.NORMAL, 100f, "Initializing...", "", MotorCommand.STOP)
    )
    val aiResult: StateFlow<EngineResult> = _aiResult.asStateFlow()

    // 1. CPR Duration Tracker
    private val _cprDurationSeconds = MutableStateFlow(0)
    val cprDurationSeconds: StateFlow<Int> = _cprDurationSeconds.asStateFlow()

    // 2. Oxygen Support States
    private val _isOxygenOn = MutableStateFlow(false)
    val isOxygenOn: StateFlow<Boolean> = _isOxygenOn.asStateFlow()

    private val _oxygenBalance = MutableStateFlow(100)
    val oxygenBalance: StateFlow<Int> = _oxygenBalance.asStateFlow()

    // 3. Emergency Stop State
    private val _isEmergencyStopActive = MutableStateFlow(false)
    val isEmergencyStopActive: StateFlow<Boolean> = _isEmergencyStopActive.asStateFlow()

    // 4. GPS State
    private val _isGpsActivated = MutableStateFlow(false)
    val isGpsActivated: StateFlow<Boolean> = _isGpsActivated.asStateFlow()

    // 5. SOS State & Tracker Data
    private val _isSosTriggered = MutableStateFlow(false)
    val isSosTriggered: StateFlow<Boolean> = _isSosTriggered.asStateFlow()

    private val _trackingData = MutableStateFlow<HelpHubClient.TrackingData?>(null)
    val trackingData: StateFlow<HelpHubClient.TrackingData?> = _trackingData.asStateFlow()

    private var trackingJob: Job? = null
    private var telemetryJob: Job? = null

    init {
        repository.connect()
        startTelemetryLoop()
        startTrackingLoop()
    }

    private fun startTelemetryLoop() {
        telemetryJob?.cancel()
        telemetryJob = viewModelScope.launch {
            observeTelemetryUseCase().collectLatest { data ->
                if (_isEmergencyStopActive.value) {
                    _telemetry.value = DEFAULT_SAFE_TELEMETRY
                    _cprDurationSeconds.value = 0
                    _aiResult.value = EngineResult(
                        status = AiStatus.NORMAL,
                        confidence = 100f,
                        recommendation = "EMERGENCY STOP ACTIVE - SAFE MODE",
                        voiceMessage = "Emergency stop active",
                        motorCommand = MotorCommand.STOP
                    )
                    repository.sendMotorCommand(0)
                    return@collectLatest
                }

                _telemetry.value = data
                
                // Process AI Rules
                val result = processAiStatusUseCase(data)
                
                // Track CPR duration during actual cardiac arrest
                if (result.status == AiStatus.CARDIAC_ARREST) {
                    _cprDurationSeconds.value += 1
                } else {
                    _cprDurationSeconds.value = 0
                }

                // Simulate oxygen support depletion
                if (_isOxygenOn.value && _oxygenBalance.value > 0) {
                    _oxygenBalance.value -= 1
                }
                
                if (result.status != _aiResult.value.status) {
                    voiceManager.queueMessage(
                        result.voiceMessage,
                        result.status == AiStatus.CARDIAC_ARREST
                    )
                }
                
                _aiResult.value = result
                
                // Send motor commands
                repository.sendMotorCommand(
                    when(result.motorCommand) {
                        MotorCommand.STOP -> 0
                        MotorCommand.LOW -> 100
                        MotorCommand.MEDIUM -> 180
                        MotorCommand.HIGH -> 255
                    }
                )

                // Sync data to firebase & HelpHub PC Server
                repository.syncToFirebase(data, result.status.name)
                helpHubClient.sendTelemetry(data, result.status.name)
            }
        }
    }

    private fun startTrackingLoop() {
        trackingJob?.cancel()
        trackingJob = viewModelScope.launch {
            while (true) {
                if (_isSosTriggered.value || _isGpsActivated.value) {
                    val data = helpHubClient.getTrackingData()
                    if (data != null) {
                        _trackingData.value = data
                    }
                }
                delay(1000)
            }
        }
    }

    fun toggleOxygen() {
        if (_isEmergencyStopActive.value) return
        _isOxygenOn.value = !_isOxygenOn.value
    }

    fun triggerEmergencyStop() {
        _isEmergencyStopActive.value = !_isEmergencyStopActive.value
        if (_isEmergencyStopActive.value) {
            // STOP ALL PROCESSES INSTANTLY
            repository.sendMotorCommand(0)
            voiceManager.setEnabled(false)
            _telemetry.value = DEFAULT_SAFE_TELEMETRY
            _cprDurationSeconds.value = 0
            _isSosTriggered.value = false
            _trackingData.value = null
            _aiResult.value = EngineResult(
                status = AiStatus.NORMAL,
                confidence = 100f,
                recommendation = "EMERGENCY STOP ACTIVE - SAFE MODE",
                voiceMessage = "Emergency stop active",
                motorCommand = MotorCommand.STOP
            )
            viewModelScope.launch {
                helpHubClient.sendEmergencyStop()
            }
        } else {
            // Re-enable and resume
            voiceManager.setEnabled(true)
            repository.connect()
            startTelemetryLoop()
        }
    }

    fun toggleGps() {
        if (_isEmergencyStopActive.value) return
        _isGpsActivated.value = !_isGpsActivated.value
        if (!_isGpsActivated.value) {
            _trackingData.value = null
        }
    }

    fun triggerSos() {
        if (_isEmergencyStopActive.value) return
        _isSosTriggered.value = true
        _isGpsActivated.value = true

        viewModelScope.launch {
            val vitals = _telemetry.value
            val status = _aiResult.value.status.name
            
            // Asynchronously shoot emergency notify payload to PC server
            val callSuccess = helpHubClient.triggerSos(vitals, status)
            
            // Save event local log history
            val newLog = SosLogEntity(
                timestamp = System.currentTimeMillis(),
                heartRate = vitals.heartRate,
                spo2 = vitals.spo2,
                compressionForce = vitals.pressure,
                compressionRate = vitals.compressionRate,
                latitude = 12.9716,
                longitude = 77.5946,
                aiSummary = "SOS ACTIVATED. Recommendations: ${_aiResult.value.recommendation}",
                notificationStatus = if (callSuccess) "HELP-HUB-ACKNOWLEDGED" else "DISPATCH-SIMULATED-LOCAL-CONTACTS"
            )
            sosLogDao.insertSosLog(newLog)
        }
    }

    fun getHelpHubMapUrl(): String {
        return "${helpHubClient.getBaseUrl()}/mobile_map"
    }

    override fun onCleared() {
        super.onCleared()
        trackingJob?.cancel()
        telemetryJob?.cancel()
        repository.disconnect()
        voiceManager.shutdown()
    }
}
