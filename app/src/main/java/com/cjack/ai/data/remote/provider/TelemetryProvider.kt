package com.cjack.ai.data.remote.provider

import com.cjack.ai.data.model.Telemetry
import kotlinx.coroutines.flow.StateFlow

interface TelemetryProvider {
    fun connect()
    fun disconnect()
    fun observeTelemetry(): StateFlow<Telemetry>
    fun sendMotorCommand(pwm: Int)
}
