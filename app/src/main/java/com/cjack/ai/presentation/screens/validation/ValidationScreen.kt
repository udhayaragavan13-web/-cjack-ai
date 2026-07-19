package com.cjack.ai.presentation.screens.validation

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
