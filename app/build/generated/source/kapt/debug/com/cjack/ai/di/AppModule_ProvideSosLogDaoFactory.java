package com.cjack.ai.di;

import com.cjack.ai.data.local.CjackDatabase;
import com.cjack.ai.data.local.dao.SosLogDao;
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
public final class AppModule_ProvideSosLogDaoFactory implements Factory<SosLogDao> {
  private final Provider<CjackDatabase> dbProvider;

  public AppModule_ProvideSosLogDaoFactory(Provider<CjackDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public SosLogDao get() {
    return provideSosLogDao(dbProvider.get());
  }

  public static AppModule_ProvideSosLogDaoFactory create(Provider<CjackDatabase> dbProvider) {
    return new AppModule_ProvideSosLogDaoFactory(dbProvider);
  }

  public static SosLogDao provideSosLogDao(CjackDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSosLogDao(db));
  }
}
