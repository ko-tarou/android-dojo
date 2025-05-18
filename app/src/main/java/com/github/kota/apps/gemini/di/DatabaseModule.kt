package com.github.kota.apps.gemini.di

import android.app.Application
import com.github.kota.apps.gemini.data.MessageDao
import com.github.kota.apps.gemini.data.MessageDatabase
import com.github.kota.apps.gemini.data.SelectedApiKeyDataStore
import com.github.kota.apps.gemini.data.TssDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMessageDatabase(application: Application): MessageDatabase {
        return MessageDatabase.getDatabase(application)
    }

    @Provides
    fun provideMessageDao(database: MessageDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    fun provideSelectedApiKeyDataStore(application: Application): SelectedApiKeyDataStore {
        return SelectedApiKeyDataStore(application)
    }

    @Provides
    fun provideTssDataStore(application: Application): TssDataStore {
        return TssDataStore(application)
    }
}