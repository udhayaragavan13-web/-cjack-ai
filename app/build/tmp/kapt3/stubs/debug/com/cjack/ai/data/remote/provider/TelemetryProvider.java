package com.cjack.ai.data.remote.provider;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0003H&J\u000e\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH&\u00a8\u0006\u000b"}, d2 = {"Lcom/cjack/ai/data/remote/provider/TelemetryProvider;", "", "connect", "", "disconnect", "observeTelemetry", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/cjack/ai/data/model/Telemetry;", "sendMotorCommand", "pwm", "", "app_debug"})
public abstract interface TelemetryProvider {
    
    public abstract void connect();
    
    public abstract void disconnect();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.StateFlow<com.cjack.ai.data.model.Telemetry> observeTelemetry();
    
    public abstract void sendMotorCommand(int pwm);
}