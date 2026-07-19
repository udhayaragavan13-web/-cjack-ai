import os
from pathlib import Path

def write_file(path, content):
    p = Path(path)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(content, encoding='utf-8')

base = Path(r"c:\dcj1\app\src\main\java\com\cjack\ai")

# 1. ECG Canvas Component
write_file(base / "presentation/components/EcgCanvas.kt", """package com.cjack.ai.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun EcgCanvas(ecgData: List<Float>, modifier: Modifier = Modifier, color: Color = Color(0xFF00FF7F)) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        if (ecgData.isEmpty()) return@Canvas

        val path = Path()
        val widthPerPoint = size.width / (ecgData.size - 1)
        
        path.moveTo(0f, size.height / 2f)

        for (i in ecgData.indices) {
            val yOffset = size.height / 2f - (ecgData[i] - 50) // Scale to canvas
            path.lineTo(i * widthPerPoint, yOffset)
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 4f)
        )
    }
}
""")

# 2. Body Map Canvas Component
write_file(base / "presentation/components/BodyMapCanvas.kt", """package com.cjack.ai.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.cjack.ai.domain.engine.AiStatus

@Composable
fun BodyMapCanvas(aiStatus: AiStatus, modifier: Modifier = Modifier) {
    val highlightColor = when(aiStatus) {
        AiStatus.NORMAL -> Color.Green
        AiStatus.WARNING -> Color.Yellow
        AiStatus.CRITICAL, AiStatus.CARDIAC_ARREST -> Color.Red
    }

    Canvas(modifier = modifier.size(200.dp)) {
        // Just a schematic outline of lungs and heart area
        val centerX = size.width / 2f
        val centerY = size.height / 2f

        // Lungs
        drawOval(
            color = Color.DarkGray,
            topLeft = Offset(centerX - 60f, centerY - 50f),
            size = androidx.compose.ui.geometry.Size(40f, 80f),
            style = Stroke(width = 4f)
        )
        drawOval(
            color = Color.DarkGray,
            topLeft = Offset(centerX + 20f, centerY - 50f),
            size = androidx.compose.ui.geometry.Size(40f, 80f),
            style = Stroke(width = 4f)
        )

        // Heart / Compression Zone
        drawCircle(
            color = highlightColor,
            radius = 30f,
            center = Offset(centerX, centerY),
        )
    }
}
""")

# 3. Main Dashboard ViewModel
write_file(base / "presentation/screens/dashboard/DashboardViewModel.kt", """package com.cjack.ai.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjack.ai.data.model.Telemetry
import com.cjack.ai.domain.engine.AiStatus
import com.cjack.ai.domain.engine.EngineResult
import com.cjack.ai.domain.engine.MotorCommand
import com.cjack.ai.domain.usecase.ObserveTelemetryUseCase
import com.cjack.ai.domain.usecase.ProcessAiStatusUseCase
import com.cjack.ai.data.repository.CjackRepository
import com.cjack.ai.domain.engine.VoiceQueueManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val observeTelemetryUseCase: ObserveTelemetryUseCase,
    private val processAiStatusUseCase: ProcessAiStatusUseCase,
    private val repository: CjackRepository,
    private val voiceManager: VoiceQueueManager
) : ViewModel() {

    private val _telemetry = MutableStateFlow(Telemetry())
    val telemetry: StateFlow<Telemetry> = _telemetry.asStateFlow()

    private val _aiResult = MutableStateFlow(
        EngineResult(AiStatus.NORMAL, 100f, "Initializing...", "", MotorCommand.STOP)
    )
    val aiResult: StateFlow<EngineResult> = _aiResult.asStateFlow()

    init {
        repository.connect()
        viewModelScope.launch {
            observeTelemetryUseCase().collectLatest { data ->
                _telemetry.value = data
                val result = processAiStatusUseCase(data)
                
                if (result.status != _aiResult.value.status) {
                    voiceManager.queueMessage(result.voiceMessage, result.status == AiStatus.CARDIAC_ARREST)
                }
                
                _aiResult.value = result
                
                // Stub: Calculate PWM based on motor command inside repo or local logic
                repository.sendMotorCommand(
                    when(result.motorCommand) {
                        MotorCommand.STOP -> 0
                        MotorCommand.LOW -> 100
                        MotorCommand.MEDIUM -> 180
                        MotorCommand.HIGH -> 255
                    }
                )

                // Sync to Firebase
                repository.syncToFirebase(data, result.status.name)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.disconnect()
        voiceManager.shutdown()
    }
}
""")

print("UI ViewModels and Components generated.")
