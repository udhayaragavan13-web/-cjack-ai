package com.cjack.ai.di;

import com.cjack.ai.domain.engine.RuleEngine;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideRuleEngineFactory implements Factory<RuleEngine> {
  @Override
  public RuleEngine get() {
    return provideRuleEngine();
  }

  public static AppModule_ProvideRuleEngineFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RuleEngine provideRuleEngine() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRuleEngine());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideRuleEngineFactory INSTANCE = new AppModule_ProvideRuleEngineFactory();
  }
}
