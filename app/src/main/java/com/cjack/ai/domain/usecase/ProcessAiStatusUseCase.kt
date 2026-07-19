package com.cjack.ai.domain.usecase

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
