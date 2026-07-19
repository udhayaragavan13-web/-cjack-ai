package com.cjack.ai.di

import android.content.Context
import androidx.room.Room
import com.cjack.ai.data.local.CjackDatabase
import com.cjack.ai.data.local.dao.CprSessionDao
import com.cjack.ai.data.local.dao.SosLogDao
import com.cjack.ai.data.remote.FirebaseService
import com.cjack.ai.data.remote.provider.ProviderFactory
import com.cjack.ai.domain.engine.RuleEngine
import com.cjack.ai.domain.engine.VoiceQueueManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CjackDatabase {
        return Room.databaseBuilder(
            context,
            CjackDatabase::class.java,
            "cjack_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideCprSessionDao(db: CjackDatabase): CprSessionDao = db.cprSessionDao

    @Provides
    fun provideSosLogDao(db: CjackDatabase): SosLogDao = db.sosLogDao

    @Provides
    @Singleton
    fun provideFirebaseService(): FirebaseService = FirebaseService()

    @Provides
    @Singleton
    fun provideProviderFactory(@ApplicationContext context: Context): ProviderFactory = ProviderFactory(context)

    @Provides
    @Singleton
    fun provideRuleEngine(): RuleEngine = RuleEngine()

    @Provides
    @Singleton
    fun provideVoiceQueueManager(@ApplicationContext context: Context): VoiceQueueManager {
        return VoiceQueueManager(context)
    }
}
