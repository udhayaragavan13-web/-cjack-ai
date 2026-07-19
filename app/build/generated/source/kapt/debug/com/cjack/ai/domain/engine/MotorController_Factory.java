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
public final class MotorController_Factory implements Factory<MotorController> {
  @Override
  public MotorController get() {
    return newInstance();
  }

  public static MotorController_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MotorController newInstance() {
    return new MotorController();
  }

  private static final class InstanceHolder {
    private static final MotorController_Factory INSTANCE = new MotorController_Factory();
  }
}
