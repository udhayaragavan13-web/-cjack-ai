package com.cjack.ai.domain.usecase;

import com.cjack.ai.domain.engine.RuleEngine;
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
public final class ProcessAiStatusUseCase_Factory implements Factory<ProcessAiStatusUseCase> {
  private final Provider<RuleEngine> ruleEngineProvider;

  public ProcessAiStatusUseCase_Factory(Provider<RuleEngine> ruleEngineProvider) {
    this.ruleEngineProvider = ruleEngineProvider;
  }

  @Override
  public ProcessAiStatusUseCase get() {
    return newInstance(ruleEngineProvider.get());
  }

  public static ProcessAiStatusUseCase_Factory create(Provider<RuleEngine> ruleEngineProvider) {
    return new ProcessAiStatusUseCase_Factory(ruleEngineProvider);
  }

  public static ProcessAiStatusUseCase newInstance(RuleEngine ruleEngine) {
    return new ProcessAiStatusUseCase(ruleEngine);
  }
}
