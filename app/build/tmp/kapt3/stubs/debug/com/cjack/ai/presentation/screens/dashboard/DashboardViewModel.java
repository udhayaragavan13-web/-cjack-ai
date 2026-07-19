package com.cjack.ai.presentation.screens.dashboard;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B7\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u0006\u00101\u001a\u000202J\b\u00103\u001a\u000204H\u0014J\b\u00105\u001a\u000204H\u0002J\b\u00106\u001a\u000204H\u0002J\u0006\u00107\u001a\u000204J\u0006\u00108\u001a\u000204J\u0006\u00109\u001a\u000204J\u0006\u0010:\u001a\u000204R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00150\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00150\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001c\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001d0\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00110\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00130\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010!R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00150\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010!R\u0017\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00150\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010!R\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00150\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010!R\u0017\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00150\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010!R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00130\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010!R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010*\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010!R\u0010\u0010,\u001a\u0004\u0018\u00010-X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0019\u0010.\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001d0\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010!R\u0010\u00100\u001a\u0004\u0018\u00010-X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006;"}, d2 = {"Lcom/cjack/ai/presentation/screens/dashboard/DashboardViewModel;", "Landroidx/lifecycle/ViewModel;", "observeTelemetryUseCase", "Lcom/cjack/ai/domain/usecase/ObserveTelemetryUseCase;", "processAiStatusUseCase", "Lcom/cjack/ai/domain/usecase/ProcessAiStatusUseCase;", "repository", "Lcom/cjack/ai/data/repository/CjackRepository;", "voiceManager", "Lcom/cjack/ai/domain/engine/VoiceQueueManager;", "helpHubClient", "Lcom/cjack/ai/data/remote/HelpHubClient;", "sosLogDao", "Lcom/cjack/ai/data/local/dao/SosLogDao;", "(Lcom/cjack/ai/domain/usecase/ObserveTelemetryUseCase;Lcom/cjack/ai/domain/usecase/ProcessAiStatusUseCase;Lcom/cjack/ai/data/repository/CjackRepository;Lcom/cjack/ai/domain/engine/VoiceQueueManager;Lcom/cjack/ai/data/remote/HelpHubClient;Lcom/cjack/ai/data/local/dao/SosLogDao;)V", "_aiResult", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/cjack/ai/domain/engine/EngineResult;", "_cprDurationSeconds", "", "_isEmergencyStopActive", "", "_isGpsActivated", "_isOxygenOn", "_isSosTriggered", "_oxygenBalance", "_telemetry", "Lcom/cjack/ai/data/model/Telemetry;", "_trackingData", "Lcom/cjack/ai/data/remote/HelpHubClient$TrackingData;", "aiResult", "Lkotlinx/coroutines/flow/StateFlow;", "getAiResult", "()Lkotlinx/coroutines/flow/StateFlow;", "cprDurationSeconds", "getCprDurationSeconds", "isEmergencyStopActive", "isGpsActivated", "isOxygenOn", "isSosTriggered", "oxygenBalance", "getOxygenBalance", "telemetry", "getTelemetry", "telemetryJob", "Lkotlinx/coroutines/Job;", "trackingData", "getTrackingData", "trackingJob", "getHelpHubMapUrl", "", "onCleared", "", "startTelemetryLoop", "startTrackingLoop", "toggleGps", "toggleOxygen", "triggerEmergencyStop", "triggerSos", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DashboardViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.cjack.ai.domain.usecase.ObserveTelemetryUseCase observeTelemetryUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.cjack.ai.domain.usecase.ProcessAiStatusUseCase processAiStatusUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.cjack.ai.data.repository.CjackRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.cjack.ai.domain.engine.VoiceQueueManager voiceManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.cjack.ai.data.remote.HelpHubClient helpHubClient = null;
    @org.jetbrains.annotations.NotNull()
    private final com.cjack.ai.data.local.dao.SosLogDao sosLogDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.cjack.ai.data.model.Telemetry> _telemetry = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.cjack.ai.data.model.Telemetry> telemetry = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.cjack.ai.domain.engine.EngineResult> _aiResult = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.cjack.ai.domain.engine.EngineResult> aiResult = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _cprDurationSeconds = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> cprDurationSeconds = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isOxygenOn = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isOxygenOn = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _oxygenBalance = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> oxygenBalance = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isEmergencyStopActive = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isEmergencyStopActive = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isGpsActivated = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isGpsActivated = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isSosTriggered = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isSosTriggered = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.cjack.ai.data.remote.HelpHubClient.TrackingData> _trackingData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.cjack.ai.data.remote.HelpHubClient.TrackingData> trackingData = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job trackingJob;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job telemetryJob;
    
    @javax.inject.Inject()
    public DashboardViewModel(@org.jetbrains.annotations.NotNull()
    com.cjack.ai.domain.usecase.ObserveTelemetryUseCase observeTelemetryUseCase, @org.jetbrains.annotations.NotNull()
    com.cjack.ai.domain.usecase.ProcessAiStatusUseCase processAiStatusUseCase, @org.jetbrains.annotations.NotNull()
    com.cjack.ai.data.repository.CjackRepository repository, @org.jetbrains.annotations.NotNull()
    com.cjack.ai.domain.engine.VoiceQueueManager voiceManager, @org.jetbrains.annotations.NotNull()
    com.cjack.ai.data.remote.HelpHubClient helpHubClient, @org.jetbrains.annotations.NotNull()
    com.cjack.ai.data.local.dao.SosLogDao sosLogDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.cjack.ai.data.model.Telemetry> getTelemetry() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.cjack.ai.domain.engine.EngineResult> getAiResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getCprDurationSeconds() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isOxygenOn() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getOxygenBalance() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isEmergencyStopActive() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isGpsActivated() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isSosTriggered() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.cjack.ai.data.remote.HelpHubClient.TrackingData> getTrackingData() {
        return null;
    }
    
    private final void startTelemetryLoop() {
    }
    
    private final void startTrackingLoop() {
    }
    
    public final void toggleOxygen() {
    }
    
    public final void triggerEmergencyStop() {
    }
    
    public final void toggleGps() {
    }
    
    public final void triggerSos() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHelpHubMapUrl() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}