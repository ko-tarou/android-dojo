package com.github.kota.apps.gemini.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.kota.apps.gemini.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    // 🔹 すべてのメッセージを取得（今までの処理）
    @Query("SELECT * FROM messages ORDER BY id ASC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    // 🔹 特定の `chatName` のメッセージを取得（新規追加）
    @Query("SELECT * FROM messages WHERE chatName = :chatName ORDER BY id ASC")
    fun getMessagesByChatName(chatName: String): Flow<List<MessageEntity>>

    // 🔹 特定の `chatName` のメッセージを削除（新規追加）
    @Query("DELETE FROM messages WHERE chatName = :chatName")
    suspend fun deleteMessagesByChatName(chatName: String)

    // 🔹 すべてのメッセージを削除（今までの処理）
    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()

    // 🔹 保存されている `chatName` のリストを取得（新規追加）
    @Query("SELECT DISTINCT chatName FROM messages")
    fun getUniqueChatNames(): Flow<List<String>>
}
