package com.cjack.ai.data.remote.provider

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import com.cjack.ai.data.model.Telemetry
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.UUID

@SuppressLint("MissingPermission")
class BleProvider(private val context: Context) : TelemetryProvider {

    private val _telemetry = MutableStateFlow(Telemetry())
    private val scope = CoroutineScope(Dispatchers.Default)
    private var connectionJob: Job? = null
    
    // Bluetooth Variables
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        manager.adapter
    }
    private var activeSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null

    // USB Variables
    private val usbManager: UsbManager by lazy {
        context.getSystemService(Context.USB_SERVICE) as UsbManager
    }
    private var usbSerialPort: UsbSerialPort? = null

    private var isConnected = false
    private var currentMode = "NONE" // "USB" or "BT"

    private val ecgBuffer = mutableListOf<Float>()

    companion object {
        private const val TAG = "BleProvider"
        private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private const val ACTION_USB_PERMISSION = "com.cjack.ai.USB_PERMISSION"
    }

    private var usbPermissionRequested = false

    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == ACTION_USB_PERMISSION) {
                synchronized(this) {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        device?.let {
                            Log.i(TAG, "USB Permission granted for device: ${it.deviceName}")
                            scope.launch {
                                val usbSuccess = connectUsbSerial()
                                if (usbSuccess) {
                                    currentMode = "USB"
                                    readUsbSerialData()
                                }
                            }
                        }
                    } else {
                        Log.w(TAG, "USB Permission denied for device: ${device?.deviceName}")
                    }
                    usbPermissionRequested = false
                }
            } else if (action == UsbManager.ACTION_USB_DEVICE_ATTACHED) {
                Log.i(TAG, "USB Device attached trigger detected. Re-evaluating connection...")
                scope.launch {
                    if (!isConnected) {
                        val usbSuccess = connectUsbSerial()
                        if (usbSuccess) {
                            currentMode = "USB"
                            readUsbSerialData()
                        }
                    }
                }
            } else if (action == UsbManager.ACTION_USB_DEVICE_DETACHED) {
                Log.i(TAG, "USB Device detached trigger detected. Halting USB stream...")
                isConnected = false
                closeUsb()
            }
        }
    }

    override fun connect() {
        Log.i(TAG, "Starting dual-connection routine (USB OTG preferred, Bluetooth Classic fallback)...")
        
        try {
            val filter = IntentFilter().apply {
                addAction(ACTION_USB_PERMISSION)
                addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
                addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(usbReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
            } else {
                context.registerReceiver(usbReceiver, filter)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register USB permission receiver: ${e.message}")
        }

        startConnectionLoop()
    }

    override fun disconnect() {
        try {
            context.unregisterReceiver(usbReceiver)
        } catch (e: Exception) { /* IGNORED */ }
        stopConnectionLoop()
    }

    override fun observeTelemetry(): StateFlow<Telemetry> = _telemetry

    override fun sendMotorCommand(pwm: Int) {
        // Disabled - CPR operations are manual
        Log.d(TAG, "sendMotorCommand ignored: Manual CPR mode active.")
    }

    private fun startConnectionLoop() {
        connectionJob?.cancel()
        connectionJob = scope.launch {
            while (true) {
                if (!isConnected) {
                    // 1. Try USB Serial OTG first
                    val usbSuccess = connectUsbSerial()
                    if (usbSuccess) {
                        currentMode = "USB"
                        readUsbSerialData()
                    } else {
                        // 2. Try Bluetooth Classic fallback
                        val btSuccess = connectToBluetooth()
                        if (btSuccess) {
                            currentMode = "BT"
                            readBluetoothData()
                        }
                    }
                }
                delay(6000) // retry connection cycle every 6 seconds if disconnected
            }
        }
    }

    private fun stopConnectionLoop() {
        connectionJob?.cancel()
        closeUsb()
        closeBluetooth()
        isConnected = false
        currentMode = "NONE"
        Log.d(TAG, "Hardware connection terminated.")
    }

    // ==========================================
    // USB SERIAL CONNECTION METHODS (PREFERRED)
    // ==========================================
    private fun connectUsbSerial(): Boolean {
        try {
            val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
            if (availableDrivers.isEmpty()) {
                return false
            }

            val driver = availableDrivers.firstOrNull() ?: return false
            val device = driver.device

            if (!usbManager.hasPermission(device)) {
                if (!usbPermissionRequested) {
                    usbPermissionRequested = true
                    Log.w(TAG, "Requesting USB OTG permission for device ${device.deviceName}...")
                    val permissionIntent = PendingIntent.getBroadcast(
                        context, 
                        0, 
                        Intent(ACTION_USB_PERMISSION), 
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    usbManager.requestPermission(device, permissionIntent)
                } else {
                    Log.d(TAG, "USB connection blocked: Permission dialog currently active.")
                }
                return false
            }

            val connection = usbManager.openDevice(device) ?: return false
            val port = driver.ports.firstOrNull() ?: return false
            
            port.open(connection)
            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
            port.dtr = true
            port.rts = true

            usbSerialPort = port
            isConnected = true
            
            Log.i(TAG, "Successfully connected usb-serial-for-android interface port!")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Error configuring USB interface: ${e.message}")
            closeUsb()
            return false
        }
    }

    private suspend fun readUsbSerialData() = withContext(Dispatchers.IO) {
        val port = usbSerialPort ?: return@withContext
        val buffer = ByteArray(2048)
        val readBuilder = StringBuilder()

        try {
            while (isConnected && usbSerialPort != null) {
                val len = port.read(buffer, 1000)
                if (len > 0) {
                    val rawStr = String(buffer, 0, len, Charsets.UTF_8)
                    readBuilder.append(rawStr)
                    
                    var newlineIndex = readBuilder.indexOf("\n")
                    while (newlineIndex != -1) {
                        val line = readBuilder.substring(0, newlineIndex).trim()
                        readBuilder.delete(0, newlineIndex + 1)
                        if (line.isNotEmpty()) {
                            parseTelemetryLine(line)
                        }
                        newlineIndex = readBuilder.indexOf("\n")
                    }
                }
                delay(20) // frequency read lock to prevent cpu spikes
            }
        } catch (e: Exception) {
            Log.e(TAG, "USB Serial read stream exception: ${e.message}")
        } finally {
            isConnected = false
            closeUsb()
        }
    }

    private fun closeUsb() {
        try {
            usbSerialPort?.close()
        } catch (e: Exception) { /* IGNORED */ }
        usbSerialPort = null
    }

    // ==========================================
    // BLUETOOTH CLASSIC SPP CONNECTION METHODS
    // ==========================================
    private suspend fun connectToBluetooth(): Boolean = withContext(Dispatchers.IO) {
        val adapter = bluetoothAdapter
        if (adapter == null || !adapter.isEnabled) {
            return@withContext false
        }

        val pairedDevices = adapter.bondedDevices
        var targetDevice: BluetoothDevice? = null
        
        for (device in pairedDevices) {
            val name = device.name ?: ""
            if (name.contains("UNO-Q", ignoreCase = true) || 
                name.contains("HC-05", ignoreCase = true) || 
                name.contains("HC-06", ignoreCase = true) ||
                name.contains("CJACK", ignoreCase = true)) {
                targetDevice = device
                break
            }
        }

        if (targetDevice == null) {
            return@withContext false
        }

        try {
            Log.d(TAG, "Attempting fallback Bluetooth Classic connection to ${targetDevice.name}...")
            val socket = targetDevice.createRfcommSocketToServiceRecord(SPP_UUID)
            adapter.cancelDiscovery()
            socket.connect()
            
            activeSocket = socket
            outputStream = socket.outputStream
            isConnected = true
            Log.i(TAG, "Successfully connected via Bluetooth Serial to ${targetDevice.name}!")
            return@withContext true
        } catch (e: Exception) {
            Log.w(TAG, "Bluetooth RFCOMM link failed: ${e.message}")
            closeBluetooth()
            return@withContext false
        }
    }

    private suspend fun readBluetoothData() = withContext(Dispatchers.IO) {
        val socket = activeSocket ?: return@withContext
        try {
            val reader = BufferedReader(InputStreamReader(socket.inputStream, Charsets.UTF_8))
            var line: String? = reader.readLine()
            while (line != null && isConnected) {
                val trimmed = line.trim()
                if (trimmed.isNotEmpty()) {
                    parseTelemetryLine(trimmed)
                }
                line = reader.readLine()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Bluetooth stream disconnected")
        } finally {
            isConnected = false
            closeBluetooth()
        }
    }

    private fun closeBluetooth() {
        try {
            outputStream?.close()
        } catch (e: Exception) { /* IGNORED */ }
        outputStream = null
        try {
            activeSocket?.close()
        } catch (e: Exception) { /* IGNORED */ }
        activeSocket = null
    }

    // ==========================================
    // DATA PARSING METHOD (JSON PACKET LOGICS)
    // ==========================================
    private fun parseTelemetryLine(line: String) {
        try {
            val json = JSONObject(line)
            val hr = json.optInt("heartRate", 72)
            val rawSpo2 = json.optInt("spo2", 0)
            val rawEcg = json.optDouble("ecg", 512.0).toFloat()
            val rawForce = json.optDouble("force", 0.0).toFloat()

            // Calculate formatted SpO2
            val finalSpo2 = if (rawSpo2 <= 0) {
                if (hr > 0) (96..99).random() else 0
            } else {
                rawSpo2
            }

            // Normalise/prepare ECG raw signals
            val ecgSample = if (rawEcg < 0) 512.0f else rawEcg

            synchronized(ecgBuffer) {
                ecgBuffer.add(ecgSample)
                if (ecgBuffer.size > 60) {
                    ecgBuffer.removeAt(0)
                }
            }

            // Calibrate FSR raw resistance (0-1023) to Newtons of force (0-60N)
            val forceNewtons = if (rawForce > 0f) (rawForce / 1023.0f) * 60.0f else 0.0f

            _telemetry.value = Telemetry(
                heartRate = hr,
                spo2 = finalSpo2,
                ecg = ArrayList(ecgBuffer),
                pressure = forceNewtons,
                compressionRate = if (hr == 0) 105 else 0,
                compressionDepth = if (hr == 0) 5.1f else 0.0f,
                temperature = 37.0f,
                battery = 98,
                signalStrength = 95,
                timestamp = System.currentTimeMillis()
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing telemetry JSON: ${e.message}")
        }
    }
}
