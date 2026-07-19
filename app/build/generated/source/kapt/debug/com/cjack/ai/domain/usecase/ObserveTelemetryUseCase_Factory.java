package com.cjack.ai.domain.usecase;

import com.cjack.ai.data.repository.CjackRepository;
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
public final class ObserveTelemetryUseCase_Factory implements Factory<ObserveTelemetryUseCase> {
  private final Provider<CjackRepository> repositoryProvider;

  public ObserveTelemetryUseCase_Factory(Provider<CjackRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ObserveTelemetryUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ObserveTelemetryUseCase_Factory create(
      Provider<CjackRepository> repositoryProvider) {
    return new ObserveTelemetryUseCase_Factory(repositoryProvider);
  }

  public static ObserveTelemetryUseCase newInstance(CjackRepository repository) {
    return new ObserveTelemetryUseCase(repository);
  }
}
