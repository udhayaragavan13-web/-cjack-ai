package com.cjack.ai.domain.engine

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
