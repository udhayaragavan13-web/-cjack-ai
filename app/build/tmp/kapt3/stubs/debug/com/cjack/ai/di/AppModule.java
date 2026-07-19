package com.cjack.ai.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\u0007\u001a\u00020\u00062\b\b\u0001\u0010\b\u001a\u00020\tH\u0007J\b\u0010\n\u001a\u00020\u000bH\u0007J\u0012\u0010\f\u001a\u00020\r2\b\b\u0001\u0010\b\u001a\u00020\tH\u0007J\b\u0010\u000e\u001a\u00020\u000fH\u0007J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010\b\u001a\u00020\tH\u0007\u00a8\u0006\u0014"}, d2 = {"Lcom/cjack/ai/di/AppModule;", "", "()V", "provideCprSessionDao", "Lcom/cjack/ai/data/local/dao/CprSessionDao;", "db", "Lcom/cjack/ai/data/local/CjackDatabase;", "provideDatabase", "context", "Landroid/content/Context;", "provideFirebaseService", "Lcom/cjack/ai/data/remote/FirebaseService;", "provideProviderFactory", "Lcom/cjack/ai/data/remote/provider/ProviderFactory;", "provideRuleEngine", "Lcom/cjack/ai/domain/engine/RuleEngine;", "provideSosLogDao", "Lcom/cjack/ai/data/local/dao/SosLogDao;", "provideVoiceQueueManager", "Lcom/cjack/ai/domain/engine/VoiceQueueManager;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.cjack.ai.di.AppModule INSTANCE = null;
    
    private AppModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.cjack.ai.data.local.CjackDatabase provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.cjack.ai.data.local.dao.CprSessionDao provideCprSessionDao(@org.jetbrains.annotations.NotNull()
    com.cjack.ai.data.local.CjackDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.cjack.ai.data.local.dao.SosLogDao provideSosLogDao(@org.jetbrains.annotations.NotNull()
    com.cjack.ai.data.local.CjackDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.cjack.ai.data.remote.FirebaseService provideFirebaseService() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.cjack.ai.data.remote.provider.ProviderFactory provideProviderFactory(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.cjack.ai.domain.engine.RuleEngine provideRuleEngine() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.cjack.ai.domain.engine.VoiceQueueManager provideVoiceQueueManager(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
}