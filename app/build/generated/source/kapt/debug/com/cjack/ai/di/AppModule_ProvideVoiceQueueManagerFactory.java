package com.cjack.ai.di;

import android.content.Context;
import com.cjack.ai.domain.engine.VoiceQueueManager;
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
public final class AppModule_ProvideVoiceQueueManagerFactory implements Factory<VoiceQueueManager> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideVoiceQueueManagerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public VoiceQueueManager get() {
    return provideVoiceQueueManager(contextProvider.get());
  }

  public static AppModule_ProvideVoiceQueueManagerFactory create(
      Provider<Context> contextProvider) {
    return new AppModule_ProvideVoiceQueueManagerFactory(contextProvider);
  }

  public static VoiceQueueManager provideVoiceQueueManager(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideVoiceQueueManager(context));
  }
}
