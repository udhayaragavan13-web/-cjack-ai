package com.cjack.ai.domain.usecase

import com.cjack.ai.data.model.Telemetry
import com.cjack.ai.data.repository.CjackRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveTelemetryUseCase @Inject constructor(
    private val repository: CjackRepository
) {
    operator fun invoke(): StateFlow<Telemetry> = repository.observeTelemetry()
}
