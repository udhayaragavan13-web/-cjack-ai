package com.cjack.ai.data.remote.provider;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ProviderFactory_Factory implements Factory<ProviderFactory> {
  private final Provider<Context> contextProvider;

  public ProviderFactory_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ProviderFactory get() {
    return newInstance(contextProvider.get());
  }

  public static ProviderFactory_Factory create(Provider<Context> contextProvider) {
    return new ProviderFactory_Factory(contextProvider);
  }

  public static ProviderFactory newInstance(Context context) {
    return new ProviderFactory(context);
  }
}
