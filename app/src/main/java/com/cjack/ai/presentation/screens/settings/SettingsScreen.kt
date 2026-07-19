package com.cjack.ai.presentation.screens.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("cjack_settings", Context.MODE_PRIVATE) }
    
    var ipVal by remember { mutableStateOf(prefs.getString("helphub_ip", "192.168.1.100") ?: "192.168.1.100") }
    var portVal by remember { mutableStateOf(prefs.getString("helphub_port", "8080") ?: "8080") }
    var contactsVal by remember { mutableStateOf(prefs.getString("emergency_contacts", "+919876543210,+918765432109") ?: "+919876543210") }
    var isSimMode by remember { mutableStateOf(prefs.getBoolean("sensor_sim_mode", true)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HELP HUB 108 CONFIGURATION", color = Color(0xFFFFEB3B), fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Emergency Control Center Settings",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Configure the Snapdragon AI PC IP address, port, and emergency dispatch numbers.",
                color = Color.LightGray,
                fontSize = 13.sp
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "NETWORK SETTINGS (SNAPDRAGON AI PC)",
                        color = Color(0xFF00E5FF),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = ipVal,
                        onValueChange = { ipVal = it },
                        label = { Text("PC IP Address") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color(0xFF00E5FF),
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color(0xFF00E5FF),
                            unfocusedBorderColor = Color.DarkGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = portVal,
                        onValueChange = { portVal = it },
                        label = { Text("PC Port") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color(0xFF00E5FF),
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color(0xFF00E5FF),
                            unfocusedBorderColor = Color.DarkGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "UNO-Q SENSOR PROTOCOL SOURCE",
                        color = Color(0xFFFFEB3B),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isSimMode) "Emulator Simulation Mode" else "Real ESP32 Microcontroller",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isSimMode) 
                                    "Generating clinical test signals locally" 
                                    else "Attempting active low-energy Bluetooth connection",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                        Switch(
                            checked = isSimMode,
                            onCheckedChange = { isSimMode = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFFFFEB3B),
                                checkedTrackColor = Color(0xFF00E5FF)
                            )
                        )
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "EMERGENCY NOTIFICATION CONTACTS",
                        color = Color(0xFFFF5252),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = contactsVal,
                        onValueChange = { contactsVal = it },
                        label = { Text("Contacts (comma-separated)") },
                        placeholder = { Text("e.g. +919876543210, +918887776665") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color(0xFFFF5252),
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color(0xFFFF5252),
                            unfocusedBorderColor = Color.DarkGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "SOS alerts are sent to all contacts in this list simultaneously.",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }

            Button(
                onClick = {
                    prefs.edit().apply {
                        putString("helphub_ip", ipVal.trim())
                        putString("helphub_port", portVal.trim())
                        putString("emergency_contacts", contactsVal.trim())
                        putBoolean("sensor_sim_mode", isSimMode)
                        apply()
                    }
                    Toast.makeText(context, "Settings saved successfully", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("SAVE CONFIGURATION", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}
