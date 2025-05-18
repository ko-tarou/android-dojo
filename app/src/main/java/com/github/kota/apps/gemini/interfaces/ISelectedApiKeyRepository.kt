package com.github.kota.apps.gemini.interfaces

import kotlinx.coroutines.flow.Flow

interface ISelectedApiKeyRepository {
    fun getSelectedApiKey(): Flow<String?>

    suspend fun saveSelectedApiKey(apiKey: String)
}