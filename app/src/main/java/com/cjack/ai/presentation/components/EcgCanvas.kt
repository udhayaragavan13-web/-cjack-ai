package com.cjack.ai.presentation.components

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
