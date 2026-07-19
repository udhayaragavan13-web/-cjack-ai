package com.cjack.ai.di;

import android.content.Context;
import com.cjack.ai.data.remote.provider.ProviderFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideProviderFactoryFactory implements Factory<ProviderFactory> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideProviderFactoryFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ProviderFactory get() {
    return provideProviderFactory(contextProvider.get());
  }

  public static AppModule_ProvideProviderFactoryFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideProviderFactoryFactory(contextProvider);
  }

  public static ProviderFactory provideProviderFactory(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideProviderFactory(context));
  }
}
