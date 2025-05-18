package com.github.kota.apps.gemini.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.kota.apps.gemini.data.ApiDatabase
import com.github.kota.apps.gemini.data.SelectedApiKeyDataStore
import com.github.kota.apps.gemini.model.ApiKeyEntity
import com.github.kota.apps.gemini.repository.ApiKeyRepository
import com.github.kota.apps.gemini.repository.SelectedApiKeyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ApiSettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val apiKeyRepository: ApiKeyRepository
    private val selectedApiKeyRepository: SelectedApiKeyRepository

    private val _apiKeys = MutableStateFlow<List<ApiKeyEntity>>(emptyList())
    val apiKeys: StateFlow<List<ApiKeyEntity>> = _apiKeys

    private val _selectedApiKey = MutableStateFlow<String?>(null)
    val selectedApiKey: StateFlow<String?> = _selectedApiKey

    init {
        val database = ApiDatabase.getDatabase(application)
        apiKeyRepository = ApiKeyRepository(database.apiKeyDao())
        selectedApiKeyRepository = SelectedApiKeyRepository(SelectedApiKeyDataStore(application))

        loadApiKeys()
        loadSelectedApiKey()
    }

    // 🔹 すべての API キーを取得
    private fun loadApiKeys() {
        viewModelScope.launch {
            apiKeyRepository.getAllApiKeys().collect { keys ->
                _apiKeys.value = keys
            }
        }
    }

    // 🔹 選択中の API キーを取得
    private fun loadSelectedApiKey() {
        viewModelScope.launch {
            selectedApiKeyRepository.getSelectedApiKey().collect { key ->
                _selectedApiKey.value = key
            }
        }
    }

    // 🔹 API キーを保存
    fun saveApiKey(newKey: String) {
        viewModelScope.launch {
            apiKeyRepository.saveApiKey(newKey)
        }
    }

    // 🔹 API キーを削除
    fun deleteApiKey(id: Int) {
        viewModelScope.launch {
            apiKeyRepository.deleteApiKey(id)
        }
    }

    // 🔹 API キーを選択 & DataStore に保存
    fun selectApiKey(apiKey: String) {
        viewModelScope.launch {
            selectedApiKeyRepository.saveSelectedApiKey(apiKey)
            _selectedApiKey.value = apiKey
        }
    }
}
