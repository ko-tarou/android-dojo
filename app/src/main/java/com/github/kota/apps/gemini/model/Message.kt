package com.github.kota.apps.gemini.model

import android.net.Uri

data class Message(
    val chatName: String?,
    val text: String?,
    val isUser: Boolean,
    val imageUri: Uri? = null
)