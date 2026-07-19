import os
from pathlib import Path

def write_file(path, content):
    p = Path(path)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(content, encoding='utf-8')

base = Path(r"c:\dcj1\app\src\main\java\com\cjack\ai")

# 1. Dashboard Screen
write_file(base / "presentation/screens/dashboard/DashboardScreen.kt", """package com.cjack.ai.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cjack.ai.domain.engine.AiStatus
import com.cjack.ai.presentation.components.BodyMapCanvas
import com.cjack.ai.presentation.components.EcgCanvas

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val telemetry by viewModel.telemetry.collectAsState()
    val aiResult by viewModel.aiResult.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CJACK AI",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            val aiColor = when(aiResult.status) {
                AiStatus.NORMAL -> Color.Green
                AiStatus.WARNING -> Color.Yellow
                else -> Color.Red
            }
            Text(
                text = aiResult.status.name,
                color = aiColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // METRICS ROW 1
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricCard("HR", "${telemetry.heartRate} bpm", Modifier.weight(1f))
            MetricCard("SpO2", "${telemetry.spo2} %", Modifier.weight(1f))
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        // METRICS ROW 2
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricCard("Compressions", "${telemetry.compressionRate}/min", Modifier.weight(1f))
            MetricCard("Depth", "${String.format("%.1f", telemetry.compressionDepth)} cm", Modifier.weight(1f))
            MetricCard("Pressure", "${String.format("%.1f", telemetry.pressure)} N", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ECG WAVEFORM
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Live ECG", color = Color.White, fontWeight = FontWeight.Bold)
                EcgCanvas(telemetry.ecg)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI RECOMMENDATION AND BODY MAP
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // AI Action
            Card(
                modifier = Modifier.weight(1f).height(200.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Command", color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(aiResult.recommendation, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Motor: ${aiResult.motorCommand.name}", color = MaterialTheme.colorScheme.secondary)
                }
            }

            // Body Map
            Card(
                modifier = Modifier.weight(1f).height(200.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    BodyMapCanvas(aiResult.status)
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}
""")

# 2. Splash Screen
write_file(base / "presentation/screens/splash/SplashScreen.kt", """package com.cjack.ai.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cjack.ai.presentation.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(key1 = true) {
        delay(2000)
        navController.navigate(Screen.Validation.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "CJACK AI",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
""")

# 3. Validation Screen
write_file(base / "presentation/screens/validation/ValidationScreen.kt", """package com.cjack.ai.presentation.screens.validation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cjack.ai.presentation.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun ValidationScreen(navController: NavController) {
    var max30102Ready by remember { mutableStateOf(false) }
    var ecgReady by remember { mutableStateOf(false) }
    var allReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        max30102Ready = true
        delay(1000)
        ecgReady = true
        allReady = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("System Validation", color = Color.White, fontSize = 28.sp)
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("MAX30102 Sensor: ${if (max30102Ready) "OK" else "Checking..."}", color = if (max30102Ready) Color.Green else Color.Yellow)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("ECG Sensor: ${if (ecgReady) "OK" else "Checking..."}", color = if (ecgReady) Color.Green else Color.Yellow)
        Spacer(modifier = Modifier.height(32.dp))
        
        if (allReady) {
            Button(onClick = { 
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Validation.route) { inclusive = true }
                }
            }) {
                Text("Start CJACK AI")
            }
        }
    }
}
""")

# 4. Other Screens
write_file(base / "presentation/screens/ambulance/AmbulanceScreen.kt", """package com.cjack.ai.presentation.screens.ambulance

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun AmbulanceScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Ambulance Dashboard Mapping Feature Pending")
    }
}
""")

print("Screens generated.")
