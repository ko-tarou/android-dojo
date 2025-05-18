package com.github.kota.apps.gemini.repository

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.github.kota.apps.gemini.interfaces.IGeminiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class GeminiRepository(private val context: Context) : IGeminiRepository {

    // 🔹 APIキーを外部から渡せるようにする
    override suspend fun getGeminiResponse(userMessage: String?, imageUri: Uri?, apiKey: String): String {
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"
                val url = URL(endpoint)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                // 🔹 画像を Base64 に変換
                val base64Image = imageUri?.let { uriToBase64(it) }

                // 🔹 JSON リクエストを作成
                val requestBody = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("role", "user")
                            put("parts", JSONArray().apply {
                                userMessage?.let {
                                    put(JSONObject().apply { put("text", it) })
                                }
                                base64Image?.let {
                                    put(JSONObject().apply {
                                        put("inline_data", JSONObject().apply {
                                            put("mime_type", "image/jpeg")
                                            put("data", it)
                                        })
                                    })
                                }
                            })
                        })
                    })
                    put("generationConfig", JSONObject().apply {
                        put("max_output_tokens", 100)
                        put("temperature", 0.7)
                    })
                }.toString()

                // 🔹 リクエスト送信
                connection.outputStream.use { outputStream ->
                    outputStream.write(requestBody.toByteArray(Charsets.UTF_8))
                    outputStream.flush()
                }

                // 🔹 応答を取得
                val responseCode = connection.responseCode
                val response = if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().readText()
                } else {
                    connection.errorStream?.bufferedReader()?.readText() ?: "エラー発生"
                }

                // 🔹 応答を解析
                val jsonResponse = JSONObject(response)
                val candidates = jsonResponse.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val firstPart = parts?.optJSONObject(0)
                val replyText = firstPart?.optString("text", "応答がありません")

                replyText ?: "応答がありません"
            } catch (e: Exception) {
                e.printStackTrace()
                "エラー発生: ${e.message}"
            } finally {
                connection?.disconnect()
            }
        }
    }

    // 🔹 画像を Base64 に変換する関数
    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            bytes?.let { Base64.encodeToString(it, Base64.NO_WRAP) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
