package com.github.kota.apps.gemini.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "api_key_prefs")

class SelectedApiKeyDataStore(context: Context) {
    private val dataStore = context.dataStore
    private val selectedApiKeyKey = stringPreferencesKey("selected_api_key")

    // 🔹 選択中の API キーを取得
    val selectedApiKey: Flow<String?> = dataStore.data.map { preferences ->
        preferences[selectedApiKeyKey]
    }

    // 🔹 選択中の API キーを保存
    suspend fun saveSelectedApiKey(apiKey: String) {
        dataStore.edit { preferences ->
            preferences[selectedApiKeyKey] = apiKey
        }
    }
}
