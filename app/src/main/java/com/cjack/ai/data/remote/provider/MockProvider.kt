package com.cjack.ai.data.remote.provider

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
        var ticks = 0
        job = scope.launch {
            while(true) {
                // Determine stable phase based on ticks (loops every 60 seconds)
                val phase = (ticks / 15) % 4
                ticks++

                val hr: Int
                val spo2: Int
                val ecgSeed: Float

                when (phase) {
                    0 -> { // NORMAL
                        hr = Random.nextInt(72, 85)
                        spo2 = Random.nextInt(97, 100)
                        ecgSeed = 50f
                    }
                    1 -> { // WARNING
                        hr = Random.nextInt(48, 55)
                        spo2 = Random.nextInt(91, 94)
                        ecgSeed = 30f
                    }
                    2 -> { // CRITICAL
                        hr = Random.nextInt(25, 35)
                        spo2 = Random.nextInt(82, 88)
                        ecgSeed = 15f
                    }
                    else -> { // CARDIAC ARREST
                        hr = 0
                        spo2 = 0
                        ecgSeed = 0f
                    }
                }

                // Generate mock ECG wave based on status
                val mockEcg = List(60) { index -> 
                    if (hr == 0) {
                        50f // Flatline
                    } else {
                        // Generate a heartbeat shape
                        val phaseVal = index % 15
                        if (phaseVal == 5) 90f else if (phaseVal == 6) 10f else 50f
                    }
                }
                
                _telemetry.value = Telemetry(
                    heartRate = hr,
                    spo2 = spo2,
                    ecg = mockEcg,
                    pressure = if (hr == 0) Random.nextFloat() * 10f else Random.nextFloat() * 40f + 10f,
                    compressionRate = if (hr == 0) 105 else 0,
                    compressionDepth = if (hr == 0) Random.nextFloat() * 2f + 4f else 0f,
                    temperature = 36.5f + Random.nextFloat(),
                    battery = Random.nextInt(90, 100),
                    signalStrength = Random.nextInt(80, 100),
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
