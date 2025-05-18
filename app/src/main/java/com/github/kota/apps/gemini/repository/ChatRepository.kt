package com.github.kota.apps.gemini.repository

import com.github.kota.apps.gemini.data.MessageDao
import com.github.kota.apps.gemini.interfaces.IChatRepository
import com.github.kota.apps.gemini.model.Message
import com.github.kota.apps.gemini.model.toEntity
import com.github.kota.apps.gemini.model.toMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val messageDao: MessageDao
) : IChatRepository {

    // 🔹 すべてのメッセージを取得
    override val messages: Flow<List<Message>> = messageDao.getAllMessages().map { entities ->
        entities.map { it.toMessage() }
    }

    // 🔹 特定の `chatName` のメッセージを取得（新規追加）
    override fun getMessagesByChatName(chatName: String): Flow<List<Message>> {
        return messageDao.getMessagesByChatName(chatName).map { entities ->
            entities.map { it.toMessage() }
        }
    }

    // 🔹 メッセージをデータベースに保存（変更なし）
    override suspend fun insertMessage(message: Message) {
        messageDao.insertMessage(message.toEntity())
    }

    // 🔹 特定の `chatName` のメッセージを削除（新規追加）
    override suspend fun deleteMessagesByChatName(chatName: String) {
        messageDao.deleteMessagesByChatName(chatName)
    }


    override fun getUniqueChatNames(): Flow<List<String>> = messageDao.getUniqueChatNames()

}
