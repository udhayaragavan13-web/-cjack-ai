package com.cjack.ai.data.remote

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.location.Location
import android.location.LocationManager
import com.cjack.ai.data.model.Telemetry
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HelpHubClient @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("cjack_settings", Context.MODE_PRIVATE)
    private var lastRecordedLocation: Pair<Double, Double> = Pair(12.9716, 77.5946)

    init {
        // Run on the main thread since requestLocationUpdates must be invoked on a thread with a Looper
        val mainHandler = android.os.Handler(android.os.Looper.getMainLooper())
        mainHandler.post {
            try {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val locationListener = object : android.location.LocationListener {
                    override fun onLocationChanged(location: Location) {
                        lastRecordedLocation = Pair(location.latitude, location.longitude)
                        Log.d("HelpHubClient", "GPS Location updated: ${location.latitude}, ${location.longitude}")
                    }
                    override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }

                // Check and request updates from both providers for real-time tracking
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000L,
                        0.5f,
                        locationListener
                    )
                }
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        1000L,
                        0.5f,
                        locationListener
                    )
                }

                // Initialize with last known location immediately
                val lastGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val lastNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                val bestLast = lastGps ?: lastNet
                if (bestLast != null) {
                    lastRecordedLocation = Pair(bestLast.latitude, bestLast.longitude)
                }
            } catch (e: SecurityException) {
                Log.e("HelpHubClient", "Security Exception: Location permission missing: ${e.message}")
            } catch (e: Exception) {
                Log.e("HelpHubClient", "Error starting active location listener: ${e.message}")
            }
        }
    }

    fun getBaseUrl(): String {
        val ip = prefs.getString("helphub_ip", "192.168.1.100") ?: "192.168.1.100"
        val port = prefs.getString("helphub_port", "8080") ?: "8080"
        return "http://$ip:$port"
    }

    fun getDeviceLocation(): Pair<Double, Double> {
        return lastRecordedLocation
    }

    fun getEmergencyContacts(): List<String> {
        val contactsStr = prefs.getString("emergency_contacts", "+919876543210,+918765432109") ?: "+919876543210"
        return contactsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    suspend fun sendTelemetry(telemetry: Telemetry, aiStatus: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("${getBaseUrl()}/api/telemetry")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            val loc = getDeviceLocation()
            val json = JSONObject().apply {
                put("heartRate", telemetry.heartRate)
                put("spo2", telemetry.spo2)
                put("ecg", JSONArray(telemetry.ecg))
                put("compressionForce", telemetry.pressure)
                put("compressionRate", telemetry.compressionRate)
                put("battery", telemetry.battery)
                put("status", aiStatus)
                put("latitude", loc.first)
                put("longitude", loc.second)
            }

            OutputStreamWriter(conn.outputStream).use { it.write(json.toString()) }
            val code = conn.responseCode
            conn.disconnect()
            code == 200
        } catch (e: Exception) {
            Log.e("HelpHubClient", "Error sending telemetry: ${e.message}")
            false
        }
    }

    private suspend fun triggerPhoneCallAndSms(message: String) = withContext(Dispatchers.Main) {
        val contacts = getEmergencyContacts()
        if (contacts.isNotEmpty()) {
            val primaryContact = contacts[0].replace(" ", "").replace("-", "")
            
            // 1. Send SMS programmatically in background thread (Parallel execution)
            @OptIn(kotlinx.coroutines.DelicateCoroutinesApi::class)
            kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
                try {
                    val smsManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        context.getSystemService(android.telephony.SmsManager::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        android.telephony.SmsManager.getDefault()
                    } ?: @Suppress("DEPRECATION") android.telephony.SmsManager.getDefault()

                    for (contact in contacts) {
                        val sanitizedContact = contact.replace(" ", "").replace("-", "")
                        smsManager.sendTextMessage(sanitizedContact, null, message, null, null)
                        Log.d("HelpHubClient", "Background SMS dispatched successfully: $sanitizedContact")
                    }
                } catch (e: Exception) {
                    Log.e("HelpHubClient", "Programmatic SMS failed: ${e.message}")
                }
            }
            
            // 2. Trigger Phone Call on Main thread at the exact same instant (Parallel execution)
            try {
                val hasCallPermission = androidx.core.content.ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.CALL_PHONE
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                
                val callIntent = if (hasCallPermission) {
                    Intent(Intent.ACTION_CALL, Uri.parse("tel:$primaryContact")).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                } else {
                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:$primaryContact")).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                }
                context.startActivity(callIntent)
                Log.d("HelpHubClient", "Call successfully launched to $primaryContact")
            } catch (e: Exception) {
                Log.e("HelpHubClient", "Error initiating Call: ${e.message}")
            }
        }
    }

    suspend fun triggerSos(telemetry: Telemetry, aiStatus: String): Boolean = withContext(Dispatchers.IO) {
        val loc = getDeviceLocation()
        val textMsg = "CJACK EMERGENCY ACTIVE! Patient needs immediate medical help. Location: https://maps.google.com/?q=${loc.first},${loc.second}. HR: ${telemetry.heartRate} BPM, SpO2: ${telemetry.spo2}%. Status: $aiStatus"

        // 1. Critical Failsafe First: Immediately trigger local phone calls and SMS (runs even if network/Wi-Fi is offline)
        try {
            triggerPhoneCallAndSms(textMsg)
        } catch (e: Exception) {
            Log.e("HelpHubClient", "Immediate Phone/SMS trigger failed: ${e.message}")
        }

        // 2. Dispatch telemetry HTTP POST to PC server
        try {
            val url = URL("${getBaseUrl()}/api/sos")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
 
            val contacts = JSONArray(getEmergencyContacts())
            val json = JSONObject().apply {
                put("heartRate", telemetry.heartRate)
                put("spo2", telemetry.spo2)
                put("ecgStatus", if (telemetry.heartRate == 0) "FLATLINE" else "ARRHYTHMIA")
                put("compressionForce", telemetry.pressure)
                put("compressionRate", telemetry.compressionRate)
                put("latitude", loc.first)
                put("longitude", loc.second)
                put("status", aiStatus)
                put("contacts", contacts)
                put("timestamp", System.currentTimeMillis())
            }
 
            OutputStreamWriter(conn.outputStream).use { it.write(json.toString()) }
            val code = conn.responseCode
            conn.disconnect()
            code == 200
        } catch (e: Exception) {
            Log.e("HelpHubClient", "Network telemetry dispatch failed: ${e.message}")
            false
        }
    }

    data class TrackingData(
        val patientLat: Double,
        val patientLng: Double,
        val ambulanceLat: Double,
        val ambulanceLng: Double,
        val route: List<Pair<Double, Double>>,
        val distanceKm: Double,
        val etaMins: Int
    )

    suspend fun getTrackingData(): TrackingData? = withContext(Dispatchers.IO) {
        try {
            val url = URL("${getBaseUrl()}/api/location/all")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            
            if (conn.responseCode == 200) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val sb = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                reader.close()
                val json = JSONObject(sb.toString())
                
                val pLoc = json.optJSONArray("patient") ?: JSONArray(listOf(12.9716, 77.5946))
                val aLoc = json.optJSONArray("ambulance") ?: JSONArray(listOf(12.9916, 77.6146))
                val routeArr = json.optJSONArray("route") ?: JSONArray()
                
                val routeList = mutableListOf<Pair<Double, Double>>()
                for (i in 0 until routeArr.length()) {
                    val pt = routeArr.getJSONArray(i)
                    routeList.add(Pair(pt.getDouble(0), pt.getDouble(1)))
                }
                
                val distance = json.optDouble("distance_km", 2.8)
                val eta = json.optInt("eta_mins", 4)
                
                TrackingData(
                    patientLat = pLoc.optDouble(0, 12.9716),
                    patientLng = pLoc.optDouble(1, 77.5946),
                    ambulanceLat = aLoc.optDouble(0, 12.9916),
                    ambulanceLng = aLoc.optDouble(1, 77.6146),
                    route = routeList,
                    distanceKm = distance,
                    etaMins = eta
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("HelpHubClient", "Error fetching tracking: ${e.message}")
            null
        }
    }

    suspend fun sendEmergencyStop(): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("${getBaseUrl()}/api/stop")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            OutputStreamWriter(conn.outputStream).use { it.write("{}") }
            val code = conn.responseCode
            conn.disconnect()
            code == 200
        } catch (e: Exception) {
            Log.e("HelpHubClient", "Error sending stop: ${e.message}")
            false
        }
    }
}
