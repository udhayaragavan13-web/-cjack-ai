package com.cjack.ai.domain.engine;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class RuleEngine_Factory implements Factory<RuleEngine> {
  @Override
  public RuleEngine get() {
    return newInstance();
  }

  public static RuleEngine_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RuleEngine newInstance() {
    return new RuleEngine();
  }

  private static final class InstanceHolder {
    private static final RuleEngine_Factory INSTANCE = new RuleEngine_Factory();
  }
}
