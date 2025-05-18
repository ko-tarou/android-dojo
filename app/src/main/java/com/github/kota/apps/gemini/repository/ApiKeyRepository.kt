package com.github.kota.apps.gemini.repository

import com.github.kota.apps.gemini.data.ApiKeyDao
import com.github.kota.apps.gemini.model.ApiKeyEntity
import kotlinx.coroutines.flow.Flow

class ApiKeyRepository(private val apiKeyDao: ApiKeyDao) {

    // すべての API キーを取得
    fun getAllApiKeys(): Flow<List<ApiKeyEntity>> = apiKeyDao.getAllApiKeys()

    // API キーを追加（上書きではなく、新規保存）
    suspend fun saveApiKey(apiKey: String) {
        apiKeyDao.insertApiKey(ApiKeyEntity(apiKey = apiKey)) // ID 自動生成
    }

    // API キーを削除
    suspend fun deleteApiKey(id: Int) {
        apiKeyDao.deleteApiKey(id)
    }
}
