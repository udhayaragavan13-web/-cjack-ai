import os
from pathlib import Path

def write_file(path, content):
    p = Path(path)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(content, encoding='utf-8')

base = Path(r"c:\dcj1\app\src\main\java\com\cjack\ai")

# 1. Models
write_file(base / "data/model/Telemetry.kt", """package com.cjack.ai.data.model

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
""")

# 2. Enums and Results for Engine
write_file(base / "domain/engine/AiStatus.kt", """package com.cjack.ai.domain.engine

enum class AiStatus {
    NORMAL, WARNING, CRITICAL, CARDIAC_ARREST
}

enum class MotorCommand {
    STOP, LOW, MEDIUM, HIGH
}

data class EngineResult(
    val status: AiStatus,
    val confidence: Float,
    val recommendation: String,
    val voiceMessage: String,
    val motorCommand: MotorCommand
)
""")

# 3. Rule Engine
write_file(base / "domain/engine/RuleEngine.kt", """package com.cjack.ai.domain.engine

import com.cjack.ai.data.model.Telemetry
import javax.inject.Inject

class RuleEngine @Inject constructor() {

    fun evaluate(telemetry: Telemetry): EngineResult {
        // Rules
        // HR > 60 && SpO2 > 95 -> NORMAL
        // HR 40-60 && SpO2 90-95 -> WARNING
        // HR < 40 && SpO2 < 90 -> CRITICAL
        // HR == 0 -> CARDIAC ARREST

        return when {
            telemetry.heartRate == 0 -> EngineResult(
                status = AiStatus.CARDIAC_ARREST,
                confidence = 99.0f,
                recommendation = "Begin CPR immediately",
                voiceMessage = "Patient Critical. Begin CPR.",
                motorCommand = MotorCommand.HIGH
            )
            telemetry.heartRate < 40 || telemetry.spo2 < 90 -> EngineResult(
                status = AiStatus.CRITICAL,
                confidence = 90.0f,
                recommendation = "Turn On Oxygen. Increase Compression.",
                voiceMessage = "Turn On Oxygen. Increase Compression.",
                motorCommand = MotorCommand.MEDIUM
            )
            telemetry.heartRate in 40..60 || telemetry.spo2 in 90..95 -> EngineResult(
                status = AiStatus.WARNING,
                confidence = 85.0f,
                recommendation = "Monitor patient closely.",
                voiceMessage = "Patient Stable, but monitor closely.",
                motorCommand = MotorCommand.LOW
            )
            else -> EngineResult(
                status = AiStatus.NORMAL,
                confidence = 95.0f,
                recommendation = "Patient Stable",
                voiceMessage = "Patient Stable",
                motorCommand = MotorCommand.STOP
            )
        }
    }
}
""")

# 4. Motor Controller
write_file(base / "domain/engine/MotorController.kt", """package com.cjack.ai.domain.engine

import javax.inject.Inject

class MotorController @Inject constructor() {
    fun calculatePWM(command: MotorCommand): Int {
        return when(command) {
            MotorCommand.STOP -> 0
            MotorCommand.LOW -> 100
            MotorCommand.MEDIUM -> 180
            MotorCommand.HIGH -> 255
        }
    }
}
""")

# 5. Voice Queue Manager
write_file(base / "domain/engine/VoiceQueueManager.kt", """package com.cjack.ai.domain.engine

import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale
import java.util.concurrent.PriorityBlockingQueue
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceQueueManager @Inject constructor(
    @ApplicationContext private val context: Context
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    data class VoiceMessage(val message: String, val isEmergency: Boolean = false) : Comparable<VoiceMessage> {
        override fun compareTo(other: VoiceMessage): Int {
            return other.isEmergency.compareTo(this.isEmergency)
        }
    }

    private val queue = PriorityBlockingQueue<VoiceMessage>()
    private var isSpeaking = false

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            isInitialized = true
            processQueue()
        }
    }

    fun queueMessage(message: String, isEmergency: Boolean = false) {
        if (!isInitialized) return
        
        queue.add(VoiceMessage(message, isEmergency))
        if(isEmergency && isSpeaking) {
            tts?.stop()
            isSpeaking = false
        }
        processQueue()
    }

    private fun processQueue() {
        if (!isSpeaking && queue.isNotEmpty()) {
            val next = queue.poll()
            next?.let {
                isSpeaking = true
                tts?.speak(it.message, TextToSpeech.QUEUE_ADD, null, it.message)
                isSpeaking = false // Ideally use UtteranceProgressListener for better accuracy
            }
        }
    }
    
    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
""")

# 6. Telemetry Providers
write_file(base / "data/remote/provider/TelemetryProvider.kt", """package com.cjack.ai.data.remote.provider

import com.cjack.ai.data.model.Telemetry
import kotlinx.coroutines.flow.StateFlow

interface TelemetryProvider {
    fun connect()
    fun disconnect()
    fun observeTelemetry(): StateFlow<Telemetry>
    fun sendMotorCommand(pwm: Int)
}
""")

write_file(base / "data/remote/provider/MockProvider.kt", """package com.cjack.ai.data.remote.provider

import com.cjack.ai.data.model.Telemetry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MockProvider : TelemetryProvider {
    
    private val _telemetry = MutableStateFlow(Telemetry())
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun connect() {
        job?.cancel()
        job = scope.launch {
            while(true) {
                // Generate mock wave
                val mockEcg = List(60) { Random.nextFloat() * 100 }
                
                _telemetry.value = Telemetry(
                    heartRate = Random.nextInt(60, 100),
                    spo2 = Random.nextInt(95, 100),
                    ecg = mockEcg,
                    pressure = Random.nextFloat() * 50f,
                    compressionRate = Random.nextInt(100, 120),
                    compressionDepth = Random.nextFloat() * 5f + 1f,
                    temperature = 36.5f + Random.nextFloat(),
                    battery = Random.nextInt(50, 100),
                    signalStrength = Random.nextInt(70, 100),
                    timestamp = System.currentTimeMillis()
                )
                delay(1000)
            }
        }
    }

    override fun disconnect() {
        job?.cancel()
    }

    override fun observeTelemetry(): StateFlow<Telemetry> = _telemetry

    override fun sendMotorCommand(pwm: Int) {
        // Output log for simulation
        println("Simulating motor command: PWM=$pwm")
    }
}
""")

write_file(base / "data/remote/provider/BleProvider.kt", """package com.cjack.ai.data.remote.provider

import com.cjack.ai.data.model.Telemetry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Stub implementation for BLE
class BleProvider : TelemetryProvider {
    
    private val _telemetry = MutableStateFlow(Telemetry())

    override fun connect() {
        // Implement real BLE connection logic
    }

    override fun disconnect() {
        // Implement real BLE disconnect logic
    }

    override fun observeTelemetry(): StateFlow<Telemetry> = _telemetry

    override fun sendMotorCommand(pwm: Int) {
        // Implement real BLE characteristic write for PWM
    }
}
""")

write_file(base / "data/remote/provider/ProviderFactory.kt", """package com.cjack.ai.data.remote.provider

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProviderFactory @Inject constructor() {
    
    fun getProvider(isSimulationMode: Boolean): TelemetryProvider {
        return if (isSimulationMode) {
            MockProvider()
        } else {
            BleProvider()
        }
    }
}
""")

print("Models and Engine completed.")
