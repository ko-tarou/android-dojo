package com.github.kota.apps.gemini.interfaces

import android.net.Uri

interface IGeminiRepository {
    suspend fun getGeminiResponse(userMessage: String?, imageUri: Uri?, apiKey: String): String
}