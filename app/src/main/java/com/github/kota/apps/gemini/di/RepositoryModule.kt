package com.github.kota.apps.gemini.di

import android.app.Application
import com.github.kota.apps.gemini.interfaces.IChatRepository
import com.github.kota.apps.gemini.interfaces.IGeminiRepository
import com.github.kota.apps.gemini.interfaces.ISelectedApiKeyRepository
import com.github.kota.apps.gemini.interfaces.ITextToSpeechRepository
import com.github.kota.apps.gemini.interfaces.ITssRepository
import com.github.kota.apps.gemini.repository.ChatRepository
import com.github.kota.apps.gemini.repository.GeminiRepository
import com.github.kota.apps.gemini.repository.SelectedApiKeyRepository
import com.github.kota.apps.gemini.repository.TextToSpeechRepository
import com.github.kota.apps.gemini.repository.TssRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindChatRepository(impl: ChatRepository): IChatRepository

    @Binds
    abstract fun bindApiKeyRepository(impl: SelectedApiKeyRepository): ISelectedApiKeyRepository

    @Binds
    abstract fun bindTssRepository(impl: TssRepository): ITssRepository
}

@Module
@InstallIn(SingletonComponent::class)
object GeminiModule {

    @Provides
    @Singleton
    fun provideGeminiRepository(app: Application): IGeminiRepository {
        return GeminiRepository(app)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object TextToSpeechModule {

    @Provides
    @Singleton
    fun provideTextToSpeechRepository(app: Application): ITextToSpeechRepository {
        return TextToSpeechRepository(app)
    }
}