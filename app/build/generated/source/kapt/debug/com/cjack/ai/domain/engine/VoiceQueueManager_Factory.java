package com.cjack.ai.domain.engine;

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
public final class VoiceQueueManager_Factory implements Factory<VoiceQueueManager> {
  private final Provider<Context> contextProvider;

  public VoiceQueueManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public VoiceQueueManager get() {
    return newInstance(contextProvider.get());
  }

  public static VoiceQueueManager_Factory create(Provider<Context> contextProvider) {
    return new VoiceQueueManager_Factory(contextProvider);
  }

  public static VoiceQueueManager newInstance(Context context) {
    return new VoiceQueueManager(context);
  }
}
