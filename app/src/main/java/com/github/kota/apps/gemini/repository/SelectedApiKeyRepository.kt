package com.github.kota.apps.gemini.repository

import com.github.kota.apps.gemini.data.SelectedApiKeyDataStore
import com.github.kota.apps.gemini.interfaces.ISelectedApiKeyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SelectedApiKeyRepository @Inject constructor(
    private val dataStore: SelectedApiKeyDataStore
) :ISelectedApiKeyRepository {

    // 🔹 現在選択中の API キーを取得
    override fun getSelectedApiKey(): Flow<String?> = dataStore.selectedApiKey

    // 🔹 選択中の API キーを保存
    override suspend fun saveSelectedApiKey(apiKey: String) {
        dataStore.saveSelectedApiKey(apiKey)
    }
}
