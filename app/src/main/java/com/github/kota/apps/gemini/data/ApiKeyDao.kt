package com.github.kota.apps.gemini.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.kota.apps.gemini.model.ApiKeyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApiKeyDao {
    @Query("SELECT * FROM api_keys")
    fun getAllApiKeys(): Flow<List<ApiKeyEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE) // 🔹 上書きしない
    suspend fun insertApiKey(apiKey: ApiKeyEntity)

    @Query("DELETE FROM api_keys WHERE id = :id")
    suspend fun deleteApiKey(id: Int)
}

