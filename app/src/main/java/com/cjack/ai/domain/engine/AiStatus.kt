package com.cjack.ai.domain.engine

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
