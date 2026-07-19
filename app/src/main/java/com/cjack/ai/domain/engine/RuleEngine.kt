package com.cjack.ai.domain.engine

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
