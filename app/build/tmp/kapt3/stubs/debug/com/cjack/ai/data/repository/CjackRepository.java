package com.cjack.ai.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B)\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0011J\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014J\u000e\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u001bJ\u0016\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u001fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n \u000f*\u0004\u0018\u00010\u000e0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/cjack/ai/data/repository/CjackRepository;", "", "context", "Landroid/content/Context;", "providerFactory", "Lcom/cjack/ai/data/remote/provider/ProviderFactory;", "firebaseService", "Lcom/cjack/ai/data/remote/FirebaseService;", "cprSessionDao", "Lcom/cjack/ai/data/local/dao/CprSessionDao;", "(Landroid/content/Context;Lcom/cjack/ai/data/remote/provider/ProviderFactory;Lcom/cjack/ai/data/remote/FirebaseService;Lcom/cjack/ai/data/local/dao/CprSessionDao;)V", "currentProvider", "Lcom/cjack/ai/data/remote/provider/TelemetryProvider;", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "connect", "", "disconnect", "observeTelemetry", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/cjack/ai/data/model/Telemetry;", "sendMotorCommand", "pwm", "", "setSimulationMode", "enabled", "", "syncToFirebase", "telemetry", "aiStatus", "", "app_debug"})
public final class CjackRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.cjack.ai.data.remote.provider.ProviderFactory providerFactory = null;
    @org.jetbrains.annotations.NotNull()
    private final com.cjack.ai.data.remote.FirebaseService firebaseService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.cjack.ai.data.local.dao.CprSessionDao cprSessionDao = null;
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private com.cjack.ai.data.remote.provider.TelemetryProvider currentProvider;
    
    @javax.inject.Inject()
    public CjackRepository(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.cjack.ai.data.remote.provider.ProviderFactory providerFactory, @org.jetbrains.annotations.NotNull()
    com.cjack.ai.data.remote.FirebaseService firebaseService, @org.jetbrains.annotations.NotNull()
    com.cjack.ai.data.local.dao.CprSessionDao cprSessionDao) {
        super();
    }
    
    public final void setSimulationMode(boolean enabled) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.cjack.ai.data.model.Telemetry> observeTelemetry() {
        return null;
    }
    
    public final void connect() {
    }
    
    public final void disconnect() {
    }
    
    public final void sendMotorCommand(int pwm) {
    }
    
    public final void syncToFirebase(@org.jetbrains.annotations.NotNull()
    com.cjack.ai.data.model.Telemetry telemetry, @org.jetbrains.annotations.NotNull()
    java.lang.String aiStatus) {
    }
}