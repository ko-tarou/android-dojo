package com.github.kota.apps.gemini.repository

import com.github.kota.apps.gemini.data.FontSizeDataStore
import kotlinx.coroutines.flow.Flow

class FontSizeRepository(private val dataStore: FontSizeDataStore) {
    fun getFontSize(): Flow<Int> = dataStore.fontSize
    suspend fun saveFontSize(size: Int) {
        dataStore.saveFontSize(size)
    }
}
