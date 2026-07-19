package com.cjack.ai.presentation.screens.dashboard;

import com.cjack.ai.data.local.dao.SosLogDao;
import com.cjack.ai.data.remote.HelpHubClient;
import com.cjack.ai.data.repository.CjackRepository;
import com.cjack.ai.domain.engine.VoiceQueueManager;
import com.cjack.ai.domain.usecase.ObserveTelemetryUseCase;
import com.cjack.ai.domain.usecase.ProcessAiStatusUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<ObserveTelemetryUseCase> observeTelemetryUseCaseProvider;

  private final Provider<ProcessAiStatusUseCase> processAiStatusUseCaseProvider;

  private final Provider<CjackRepository> repositoryProvider;

  private final Provider<VoiceQueueManager> voiceManagerProvider;

  private final Provider<HelpHubClient> helpHubClientProvider;

  private final Provider<SosLogDao> sosLogDaoProvider;

  public DashboardViewModel_Factory(
      Provider<ObserveTelemetryUseCase> observeTelemetryUseCaseProvider,
      Provider<ProcessAiStatusUseCase> processAiStatusUseCaseProvider,
      Provider<CjackRepository> repositoryProvider,
      Provider<VoiceQueueManager> voiceManagerProvider,
      Provider<HelpHubClient> helpHubClientProvider, Provider<SosLogDao> sosLogDaoProvider) {
    this.observeTelemetryUseCaseProvider = observeTelemetryUseCaseProvider;
    this.processAiStatusUseCaseProvider = processAiStatusUseCaseProvider;
    this.repositoryProvider = repositoryProvider;
    this.voiceManagerProvider = voiceManagerProvider;
    this.helpHubClientProvider = helpHubClientProvider;
    this.sosLogDaoProvider = sosLogDaoProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(observeTelemetryUseCaseProvider.get(), processAiStatusUseCaseProvider.get(), repositoryProvider.get(), voiceManagerProvider.get(), helpHubClientProvider.get(), sosLogDaoProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<ObserveTelemetryUseCase> observeTelemetryUseCaseProvider,
      Provider<ProcessAiStatusUseCase> processAiStatusUseCaseProvider,
      Provider<CjackRepository> repositoryProvider,
      Provider<VoiceQueueManager> voiceManagerProvider,
      Provider<HelpHubClient> helpHubClientProvider, Provider<SosLogDao> sosLogDaoProvider) {
    return new DashboardViewModel_Factory(observeTelemetryUseCaseProvider, processAiStatusUseCaseProvider, repositoryProvider, voiceManagerProvider, helpHubClientProvider, sosLogDaoProvider);
  }

  public static DashboardViewModel newInstance(ObserveTelemetryUseCase observeTelemetryUseCase,
      ProcessAiStatusUseCase processAiStatusUseCase, CjackRepository repository,
      VoiceQueueManager voiceManager, HelpHubClient helpHubClient, SosLogDao sosLogDao) {
    return new DashboardViewModel(observeTelemetryUseCase, processAiStatusUseCase, repository, voiceManager, helpHubClient, sosLogDao);
  }
}
