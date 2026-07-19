package com.cjack.ai.presentation.screens.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cjack.ai.domain.engine.AiStatus
import com.cjack.ai.data.remote.HelpHubClient
import com.cjack.ai.presentation.components.BodyMapCanvas
import com.cjack.ai.presentation.components.EcgCanvas
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.model.CameraPosition

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val telemetry by viewModel.telemetry.collectAsState()
    val aiResult by viewModel.aiResult.collectAsState()
    val cprDuration by viewModel.cprDurationSeconds.collectAsState()
    val isOxygenOn by viewModel.isOxygenOn.collectAsState()
    val oxygenBalance by viewModel.oxygenBalance.collectAsState()
    val isEmergencyStopActive by viewModel.isEmergencyStopActive.collectAsState()
    val isGpsActivated by viewModel.isGpsActivated.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)) // Sleek slate dark background
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP HEADER BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CJACK AI CLINICAL CONTROL DASHBOARD",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "UNO-Q BLE ACTIVE",
                    color = Color(0xFF00E5FF),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .border(1.dp, Color(0xFF00E5FF), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(onClick = { navController.navigate("settings") }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }
        }

        // ============================================
        // ROW 1: TOP VITALS ROW (5 horizontal cards as sketched)
        // ============================================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val cprMin = cprDuration / 60
            val cprSec = cprDuration % 60
            val durationText = String.format("%02d:%02d", cprMin, cprSec)

            MetricCard(
                title = "SpO2",
                value = "${telemetry.spo2}.0%",
                sublabel = if (telemetry.spo2 >= 95) "NORMAL" else if (telemetry.spo2 >= 90) "WARNING" else "CRITICAL",
                highlightColor = if (telemetry.spo2 >= 95) Color.Green else if (telemetry.spo2 >= 90) Color.Yellow else Color.Red,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Compression Rate",
                value = "${telemetry.compressionRate} CPM",
                sublabel = if (telemetry.compressionRate in 100..120) "NORMAL" else "OPTIMAL REACHED",
                highlightColor = if (telemetry.compressionRate in 100..120) Color.Green else Color.Yellow,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "CPR Force",
                value = "${String.format("%.1f", telemetry.pressure)} N",
                sublabel = if (telemetry.pressure >= 30f) "NORMAL" else "INSUFFICIENT",
                highlightColor = if (telemetry.pressure >= 30f) Color.Green else Color.Yellow,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "CPR Duration",
                value = durationText,
                sublabel = "ELAPSED TIME",
                highlightColor = if (cprDuration > 0) Color.Red else Color.Gray,
                modifier = Modifier.weight(1f)
            )
        }

        // ============================================
        // ROW 2: INTERMEDIATE CONTROLS & GRAPH ROW (Middle segment)
        // ============================================
        Row(
            modifier = Modifier.fillMaxWidth().height(230.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // A. OXYGEN SUPPORT CARD
            Card(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "OXYGEN SUPPORT",
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFFFFEB3B), shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("DURATION: ${oxygenBalance} mins", color = Color.White, fontSize = 14.sp)
                        Text("BALANCE: ${oxygenBalance}%", color = Color.LightGray, fontSize = 13.sp)
                        
                        // Cylinder bar visual representation
                        LinearProgressIndicator(
                            progress = oxygenBalance / 100f,
                            color = Color(0xFF00E5FF),
                            trackColor = Color.DarkGray,
                            modifier = Modifier.fillMaxWidth().height(8.dp)
                        )
                    }

                    // Dynamic Toggle Switch
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isOxygenOn) Color(0xFF0284C7) else Color.DarkGray,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { viewModel.toggleOxygen() }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isOxygenOn) "ON" else "OFF",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // B. EMERGENCY STOP CARD
            Card(
                modifier = Modifier.weight(1.1f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "EMERGENCY CUTOFF",
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFFFFEB3B), shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )

                    Button(
                        onClick = { viewModel.triggerEmergencyStop() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEmergencyStopActive) Color(0xFF22C55E) else Color(0xFFEF4444)
                        ),
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isEmergencyStopActive) "RESUME" else "EMERGENCY\nSTOP",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Text(
                        text = if (isEmergencyStopActive) "SYSTEM STOPPED" else "ACTIVE RUNNING",
                        color = if (isEmergencyStopActive) Color.Red else Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }

            // C. HEART RATE & ECG GRAPH CARD
            Card(
                modifier = Modifier.weight(1.5f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "HEART RATE",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color(0xFFFFEB3B), shape = RoundedCornerShape(2.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                        Text(
                            text = "${telemetry.heartRate}.0 bpm",
                            color = Color(0xFF00E5FF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    // Waveform placement
                    Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                        EcgCanvas(telemetry.ecg)
                    }

                    Text(
                        text = if (telemetry.heartRate == 0) "FLATLINE SIGNAL DETECTED" else "STABLE RHYTHM",
                        color = if (telemetry.heartRate == 0) Color.Red else Color.LightGray,
                        fontSize = 10.sp
                    )
                }
            }

            // D. CONDITION MONITORING CARD
            Card(
                modifier = Modifier.weight(1.4f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CONDITION MONITORING",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color(0xFFFFEB3B), shape = RoundedCornerShape(2.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                        
                        val aiColor = when(aiResult.status) {
                            AiStatus.NORMAL -> Color.Green
                            AiStatus.WARNING -> Color.Yellow
                            else -> Color.Red
                        }
                        Text(
                            text = aiResult.status.name,
                            color = aiColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    // Body Visualizer placement
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        BodyMapCanvas(aiResult.status, modifier = Modifier.fillMaxHeight())
                    }
                }
            }
        }

        // ============================================
        // ROW 3: BOTTOM MAP & AMBULANCE TRACKER ROW
        // ============================================
        Row(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // I. MAP VISUAL CONTAINER
            Card(
                modifier = Modifier.weight(1.5f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp).fillMaxSize()) {
                    Text(
                        text = "PATIENT GPS MAP",
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFFFFEB3B), shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color(0xFF0F172A), RoundedCornerShape(6.dp))
                    ) {
                        val trackingData by viewModel.trackingData.collectAsState()
                        if (isGpsActivated) {
                            AndroidView(
                                modifier = Modifier.fillMaxSize(),
                                factory = { context ->
                                    WebView(context).apply {
                                        settings.javaScriptEnabled = true
                                        settings.domStorageEnabled = true
                                        webViewClient = object : WebViewClient() {
                                            override fun onReceivedError(
                                                view: WebView?,
                                                errorCode: Int,
                                                description: String?,
                                                failingUrl: String?
                                            ) {
                                                val errorHtml = """
                                                    <html>
                                                    <body style="background-color:#0f172a; color:#ff5252; font-family:sans-serif; text-align:center; padding: 20px; padding-top:40px; margin:0;">
                                                        <h3 style="color:#00e5ff; margin-bottom:8px; font-size:16px; letter-spacing:1px;">CONNECTION PENDING</h3>
                                                        <p style="font-size:12px; color:#94a3b8; line-height:1.5; margin-bottom:20px;">
                                                            Unable to reach Help Hub at:<br>
                                                            <span style="color:#ffffff; word-break:break-all;">$failingUrl</span>
                                                        </p>
                                                        <div style="font-size:11px; color:#cbd5e1; border: 1px dashed #334155; padding:12px; border-radius:6px; background-color:#1e293b;">
                                                            Please tap the <b>Settings Gear</b> icon at the top right and enter your Snapdragon PC's local IP address.
                                                        </div>
                                                    </body>
                                                    </html>
                                                """.trimIndent()
                                                view?.loadDataWithBaseURL(null, errorHtml, "text/html", "UTF-8", null)
                                            }
                                        }
                                        loadUrl(viewModel.getHelpHubMapUrl())
                                    }
                                },
                                update = { webView ->
                                    val currentUrl = viewModel.getHelpHubMapUrl()
                                    if (webView.url != currentUrl) {
                                        webView.loadUrl(currentUrl)
                                    }
                                }
                            )
                            
                            // Map overlay info
                            trackingData?.let { track ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xDD0F172A)),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.padding(8.dp).align(Alignment.TopStart)
                                ) {
                                    Text(
                                        text = "Dist: ${String.format("%.2f", track.distanceKm)} km\nETA: ${track.etaMins} mins",
                                        color = Color(0xFF00E5FF),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                            }
                        } else {
                            MockMapCanvas(modifier = Modifier.fillMaxSize(), trackingData = null)
                        }
                        
                        Text(
                            text = if (isGpsActivated) "LIVE HELP HUB 108 MAP" else "MAP STANDBY",
                            color = if (isGpsActivated) Color(0xFF00E5FF) else Color.Gray,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(8.dp).align(Alignment.BottomEnd)
                        )
                    }
                }
            }

            // II. AMBULANCE LIVE TRACKING CARD
            Card(
                modifier = Modifier.weight(1.5f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "AMBULANCE LIVE TRACKING",
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFFFFEB3B), shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "GPS: ${if (isGpsActivated) "ACTIVE" else "DEACTIVATED"}",
                            color = if (isGpsActivated) Color(0xFF00E5FF) else Color.LightGray,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )

                        // GPS Activate Switch Button
                        Button(
                            onClick = { viewModel.toggleGps() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isGpsActivated) Color(0xFF0284C7) else Color.DarkGray
                            ),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(if (isGpsActivated) "STOP GPS" else "ACTIVATE", fontSize = 12.sp)
                        }
                    }

                    val isSosTriggered by viewModel.isSosTriggered.collectAsState()
                    // Emergency dispatcher call button (SOS)
                    Button(
                        onClick = {
                            viewModel.triggerSos()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSosTriggered) Color(0xFFFF1744) else Color(0xFFEF4444)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (isSosTriggered) "SOS DISPATCHED" else "SOS EMERGENCY CALL",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // III. SYSTEM STATE / BATTERY CARD
            Card(
                modifier = Modifier.weight(0.8f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "BATTERY",
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFFFFEB3B), shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )

                    // Large circular/bar battery representation
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${telemetry.battery}%",
                            color = if (telemetry.battery > 20) Color.Green else Color.Red,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "VOLTAGE STABLE", color = Color.Gray, fontSize = 9.sp)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(Color.DarkGray, RoundedCornerShape(5.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(telemetry.battery / 100f)
                                .background(
                                    if (telemetry.battery > 20) Color.Green else Color.Red,
                                    RoundedCornerShape(5.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    sublabel: String,
    highlightColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title.uppercase(),
                color = Color.Black,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(Color(0xFFFFEB3B), shape = RoundedCornerShape(2.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
            
            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = sublabel,
                color = highlightColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun MockMapCanvas(modifier: Modifier = Modifier, trackingData: HelpHubClient.TrackingData?) {
    val transition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        // Draw cyber blue grid
        val gridSize = 25f
        for (i in 0..(width / gridSize).toInt()) {
            drawLine(
                color = Color(0xFF00E5FF).copy(alpha = 0.08f),
                start = Offset(i * gridSize, 0f),
                end = Offset(i * gridSize, height),
                strokeWidth = 1f
            )
        }
        for (i in 0..(height / gridSize).toInt()) {
            drawLine(
                color = Color(0xFF00E5FF).copy(alpha = 0.08f),
                start = Offset(0f, i * gridSize),
                end = Offset(width, i * gridSize),
                strokeWidth = 1f
            )
        }

        // Coordinates
        val patientX = width * 0.5f
        val patientY = height * 0.5f
        
        val startAmbX = width * 0.15f
        val startAmbY = height * 0.15f
        
        // Dynamic ambulance placement
        val maxDist = 2.8
        val currentDist = trackingData?.distanceKm ?: maxDist
        val progress = ((maxDist - currentDist) / maxDist).coerceIn(0.0, 1.0).toFloat()
        
        val ambX = startAmbX + (patientX - startAmbX) * progress
        val ambY = startAmbY + (patientY - startAmbY) * progress

        // Draw simulated highway paths
        drawLine(
            color = Color.Gray.copy(alpha = 0.25f),
            start = Offset(0f, height * 0.5f),
            end = Offset(width, height * 0.5f),
            strokeWidth = 8f
        )
        drawLine(
            color = Color.Gray.copy(alpha = 0.25f),
            start = Offset(width * 0.5f, 0f),
            end = Offset(width * 0.5f, height),
            strokeWidth = 8f
        )
        drawLine(
            color = Color.Gray.copy(alpha = 0.2f),
            start = Offset(0f, 0f),
            end = Offset(width, height),
            strokeWidth = 4f
        )

        // Draw tracking route (cyan line)
        if (trackingData != null) {
            drawLine(
                color = Color(0xFF00E5FF),
                start = Offset(ambX, ambY),
                end = Offset(patientX, patientY),
                strokeWidth = 6f
            )
        }

        // Heart attack patient beacon (pulsing red)
        drawCircle(
            color = Color.Red.copy(alpha = if (trackingData != null) pulseAlpha else 0.25f),
            radius = 24f,
            center = Offset(patientX, patientY)
        )
        drawCircle(
            color = Color.Red,
            radius = 6f,
            center = Offset(patientX, patientY)
        )

        // Ambulance marker node
        drawCircle(
            color = Color(0xFFFFFF00).copy(alpha = pulseAlpha),
            radius = 12f,
            center = Offset(ambX, ambY)
        )
        drawCircle(
            color = Color(0xFF00E5FF),
            radius = 7f,
            center = Offset(ambX, ambY)
        )
    }
}

