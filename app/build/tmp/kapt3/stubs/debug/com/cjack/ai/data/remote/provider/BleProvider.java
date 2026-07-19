package com.cjack.ai.data.remote.provider;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0004\b\u0007\u0018\u0000 :2\u00020\u0001:\u0001:B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\'\u001a\u00020(H\u0002J\b\u0010)\u001a\u00020(H\u0002J\b\u0010*\u001a\u00020(H\u0016J\u000e\u0010+\u001a\u00020\u0018H\u0082@\u00a2\u0006\u0002\u0010,J\b\u0010-\u001a\u00020\u0018H\u0002J\b\u0010.\u001a\u00020(H\u0016J\u000e\u0010/\u001a\b\u0012\u0004\u0012\u00020\u000700H\u0016J\u0010\u00101\u001a\u00020(2\u0006\u00102\u001a\u00020\u0013H\u0002J\u000e\u00103\u001a\u00020(H\u0082@\u00a2\u0006\u0002\u0010,J\u000e\u00104\u001a\u00020(H\u0082@\u00a2\u0006\u0002\u0010,J\u0010\u00105\u001a\u00020(2\u0006\u00106\u001a\u000207H\u0016J\b\u00108\u001a\u00020(H\u0002J\b\u00109\u001a\u00020(H\u0002R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001d\u0010\n\u001a\u0004\u0018\u00010\u000b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\rR\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001d\u001a\u00020\u001e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b!\u0010\u000f\u001a\u0004\b\u001f\u0010 R\u000e\u0010\"\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u0004\u0018\u00010&X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006;"}, d2 = {"Lcom/cjack/ai/data/remote/provider/BleProvider;", "Lcom/cjack/ai/data/remote/provider/TelemetryProvider;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "_telemetry", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/cjack/ai/data/model/Telemetry;", "activeSocket", "Landroid/bluetooth/BluetoothSocket;", "bluetoothAdapter", "Landroid/bluetooth/BluetoothAdapter;", "getBluetoothAdapter", "()Landroid/bluetooth/BluetoothAdapter;", "bluetoothAdapter$delegate", "Lkotlin/Lazy;", "connectionJob", "Lkotlinx/coroutines/Job;", "currentMode", "", "ecgBuffer", "", "", "isConnected", "", "outputStream", "Ljava/io/OutputStream;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "usbManager", "Landroid/hardware/usb/UsbManager;", "getUsbManager", "()Landroid/hardware/usb/UsbManager;", "usbManager$delegate", "usbPermissionRequested", "usbReceiver", "Landroid/content/BroadcastReceiver;", "usbSerialPort", "Lcom/hoho/android/usbserial/driver/UsbSerialPort;", "closeBluetooth", "", "closeUsb", "connect", "connectToBluetooth", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "connectUsbSerial", "disconnect", "observeTelemetry", "Lkotlinx/coroutines/flow/StateFlow;", "parseTelemetryLine", "line", "readBluetoothData", "readUsbSerialData", "sendMotorCommand", "pwm", "", "startConnectionLoop", "stopConnectionLoop", "Companion", "app_debug"})
@android.annotation.SuppressLint(value = {"MissingPermission"})
public final class BleProvider implements com.cjack.ai.data.remote.provider.TelemetryProvider {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.cjack.ai.data.model.Telemetry> _telemetry = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job connectionJob;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy bluetoothAdapter$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private android.bluetooth.BluetoothSocket activeSocket;
    @org.jetbrains.annotations.Nullable()
    private java.io.OutputStream outputStream;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy usbManager$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private com.hoho.android.usbserial.driver.UsbSerialPort usbSerialPort;
    private boolean isConnected = false;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String currentMode = "NONE";
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Float> ecgBuffer = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "BleProvider";
    @org.jetbrains.annotations.NotNull()
    private static final java.util.UUID SPP_UUID = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String ACTION_USB_PERMISSION = "com.cjack.ai.USB_PERMISSION";
    private boolean usbPermissionRequested = false;
    @org.jetbrains.annotations.NotNull()
    private final android.content.BroadcastReceiver usbReceiver = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.cjack.ai.data.remote.provider.BleProvider.Companion Companion = null;
    
    public BleProvider(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final android.bluetooth.BluetoothAdapter getBluetoothAdapter() {
        return null;
    }
    
    private final android.hardware.usb.UsbManager getUsbManager() {
        return null;
    }
    
    @java.lang.Override()
    public void connect() {
    }
    
    @java.lang.Override()
    public void disconnect() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.StateFlow<com.cjack.ai.data.model.Telemetry> observeTelemetry() {
        return null;
    }
    
    @java.lang.Override()
    public void sendMotorCommand(int pwm) {
    }
    
    private final void startConnectionLoop() {
    }
    
    private final void stopConnectionLoop() {
    }
    
    private final boolean connectUsbSerial() {
        return false;
    }
    
    private final java.lang.Object readUsbSerialData(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void closeUsb() {
    }
    
    private final java.lang.Object connectToBluetooth(kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final java.lang.Object readBluetoothData(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void closeBluetooth() {
    }
    
    private final void parseTelemetryLine(java.lang.String line) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/cjack/ai/data/remote/provider/BleProvider$Companion;", "", "()V", "ACTION_USB_PERMISSION", "", "SPP_UUID", "Ljava/util/UUID;", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}