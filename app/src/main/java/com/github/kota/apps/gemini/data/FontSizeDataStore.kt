package com.github.kota.apps.gemini.data

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "fontSize")

class FontSizeDataStore(private val context: Context) {
    companion object {
        private val FONT_SIZE_KEY = intPreferencesKey("font_size")
        private const val DEFAULT_FONT_SIZE = 16
    }

    val fontSize: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[FONT_SIZE_KEY] ?: DEFAULT_FONT_SIZE
        }

    suspend fun saveFontSize(size: Int) {
        context.dataStore.edit { preferences ->
            preferences[FONT_SIZE_KEY] = size
        }
    }
}
