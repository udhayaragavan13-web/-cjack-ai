package com.cjack.ai.data.remote.provider;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\u000bH\u0016J\u000e\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\u000eH\u0016J\u0010\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/cjack/ai/data/remote/provider/MockProvider;", "Lcom/cjack/ai/data/remote/provider/TelemetryProvider;", "()V", "_telemetry", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/cjack/ai/data/model/Telemetry;", "job", "Lkotlinx/coroutines/Job;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "connect", "", "disconnect", "observeTelemetry", "Lkotlinx/coroutines/flow/StateFlow;", "sendMotorCommand", "pwm", "", "app_debug"})
public final class MockProvider implements com.cjack.ai.data.remote.provider.TelemetryProvider {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.cjack.ai.data.model.Telemetry> _telemetry = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job job;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    
    public MockProvider() {
        super();
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
}