package com.github.kota.apps.gemini.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chatName: String?,
    val text: String?,
    val isUser: Boolean,
    val imageUri: String? = null
)

fun MessageEntity.toMessage(): Message {
    return Message(
        chatName = this.chatName,
        text = this.text,
        isUser = this.isUser,
        imageUri = this.imageUri?.let { Uri.parse(it) }
    )
}

fun Message.toEntity(): MessageEntity {
    return MessageEntity(
        chatName = this.chatName,
        text = this.text,
        isUser = this.isUser,
        imageUri = this.imageUri?.toString()
    )
}
