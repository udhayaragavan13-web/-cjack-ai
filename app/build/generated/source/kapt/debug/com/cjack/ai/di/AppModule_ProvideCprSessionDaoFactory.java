package com.cjack.ai.di;

import com.cjack.ai.data.local.CjackDatabase;
import com.cjack.ai.data.local.dao.CprSessionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideCprSessionDaoFactory implements Factory<CprSessionDao> {
  private final Provider<CjackDatabase> dbProvider;

  public AppModule_ProvideCprSessionDaoFactory(Provider<CjackDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CprSessionDao get() {
    return provideCprSessionDao(dbProvider.get());
  }

  public static AppModule_ProvideCprSessionDaoFactory create(Provider<CjackDatabase> dbProvider) {
    return new AppModule_ProvideCprSessionDaoFactory(dbProvider);
  }

  public static CprSessionDao provideCprSessionDao(CjackDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCprSessionDao(db));
  }
}
