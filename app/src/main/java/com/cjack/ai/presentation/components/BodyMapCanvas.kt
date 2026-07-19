package com.cjack.ai.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cjack.ai.R
import com.cjack.ai.domain.engine.AiStatus

@Composable
fun BodyMapCanvas(aiStatus: AiStatus, modifier: Modifier = Modifier) {
    val highlightColor = when(aiStatus) {
        AiStatus.NORMAL -> Color.Green
        AiStatus.WARNING -> Color.Yellow
        AiStatus.CRITICAL, AiStatus.CARDIAC_ARREST -> Color.Red
    }

    // Hologram pulse transition for CPR target zone
    val infiniteTransition = rememberInfiniteTransition(label = "hologram_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(200.dp)
            .drawWithContent {
                // Draw the underlying body projection vector first
                drawContent()

                // Calculate center chest coordinate positions based on ic_body_hologram properties
                val centerX = size.width / 2f
                val centerY = size.height * 0.3416f // mapped directly to chest height of the scanning cylinder
                val scale = size.width / 180f

                // Glowing outer target ring
                drawCircle(
                    color = highlightColor.copy(alpha = 0.35f),
                    radius = 16f * scale * pulseScale,
                    center = Offset(centerX, centerY)
                )

                // Neon inner core dot
                drawCircle(
                    color = highlightColor,
                    radius = 5f * scale,
                    center = Offset(centerX, centerY)
                )
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_body_hologram),
            contentDescription = "Hologram Body scan",
            modifier = Modifier.fillMaxSize()
        )
    }
}


