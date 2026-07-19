package com.cjack.ai.domain.usecase;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0086\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/cjack/ai/domain/usecase/ObserveTelemetryUseCase;", "", "repository", "Lcom/cjack/ai/data/repository/CjackRepository;", "(Lcom/cjack/ai/data/repository/CjackRepository;)V", "invoke", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/cjack/ai/data/model/Telemetry;", "app_debug"})
public final class ObserveTelemetryUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.cjack.ai.data.repository.CjackRepository repository = null;
    
    @javax.inject.Inject()
    public ObserveTelemetryUseCase(@org.jetbrains.annotations.NotNull()
    com.cjack.ai.data.repository.CjackRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.cjack.ai.data.model.Telemetry> invoke() {
        return null;
    }
}