package com.cjack.ai.data.repository;

import android.content.Context;
import com.cjack.ai.data.local.dao.CprSessionDao;
import com.cjack.ai.data.remote.FirebaseService;
import com.cjack.ai.data.remote.provider.ProviderFactory;
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
public final class CjackRepository_Factory implements Factory<CjackRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<ProviderFactory> providerFactoryProvider;

  private final Provider<FirebaseService> firebaseServiceProvider;

  private final Provider<CprSessionDao> cprSessionDaoProvider;

  public CjackRepository_Factory(Provider<Context> contextProvider,
      Provider<ProviderFactory> providerFactoryProvider,
      Provider<FirebaseService> firebaseServiceProvider,
      Provider<CprSessionDao> cprSessionDaoProvider) {
    this.contextProvider = contextProvider;
    this.providerFactoryProvider = providerFactoryProvider;
    this.firebaseServiceProvider = firebaseServiceProvider;
    this.cprSessionDaoProvider = cprSessionDaoProvider;
  }

  @Override
  public CjackRepository get() {
    return newInstance(contextProvider.get(), providerFactoryProvider.get(), firebaseServiceProvider.get(), cprSessionDaoProvider.get());
  }

  public static CjackRepository_Factory create(Provider<Context> contextProvider,
      Provider<ProviderFactory> providerFactoryProvider,
      Provider<FirebaseService> firebaseServiceProvider,
      Provider<CprSessionDao> cprSessionDaoProvider) {
    return new CjackRepository_Factory(contextProvider, providerFactoryProvider, firebaseServiceProvider, cprSessionDaoProvider);
  }

  public static CjackRepository newInstance(Context context, ProviderFactory providerFactory,
      FirebaseService firebaseService, CprSessionDao cprSessionDao) {
    return new CjackRepository(context, providerFactory, firebaseService, cprSessionDao);
  }
}
