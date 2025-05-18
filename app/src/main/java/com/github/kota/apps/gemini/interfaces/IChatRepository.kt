package com.github.kota.apps.gemini.interfaces

import com.github.kota.apps.gemini.model.Message
import kotlinx.coroutines.flow.Flow

interface IChatRepository {
    val messages: Flow<List<Message>>
    fun getMessagesByChatName(chatName: String): Flow<List<Message>>
    suspend fun insertMessage(message: Message)
    suspend fun deleteMessagesByChatName(chatName: String)
    fun getUniqueChatNames(): Flow<List<String>>
}
