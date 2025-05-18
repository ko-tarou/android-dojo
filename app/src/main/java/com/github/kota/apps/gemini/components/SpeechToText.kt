package com.github.kota.apps.gemini.components

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun SpeechToText(onResult: (String) -> Unit) {
    val context = LocalContext.current
    var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }
    var isListening by remember { mutableStateOf(false) }

    // マイクの権限リクエスト
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "マイクの権限を許可してください", Toast.LENGTH_SHORT).show()
        }
    }

    fun startListening() {
        Log.d("SpeechToText", "startListening() が呼ばれた")

        if (speechRecognizer == null) {
            Log.d("SpeechToText", "SpeechRecognizer を新しく作成")
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        Log.d("SpeechToText", "音声入力の準備完了")
                        Toast.makeText(context, "音声入力を開始...", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResults(results: Bundle?) {
                        Log.d("SpeechToText", "onResults() が呼ばれた！")

                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (matches.isNullOrEmpty()) {
                            Log.e("SpeechToText", "onResults() で認識結果がない")
                            Toast.makeText(context, "認識結果がありません", Toast.LENGTH_SHORT).show()
                            return
                        }

                        val result = matches.firstOrNull().orEmpty()
                        Log.d("SpeechToText", "音声認識結果: $result")
                        Toast.makeText(context, "認識結果: $result", Toast.LENGTH_SHORT).show()

                        onResult(result) // 🔹 `TextField` に反映！
                    }


                    override fun onError(error: Int) {
                        isListening = false
                        val errorMessage = when (error) {
                            SpeechRecognizer.ERROR_AUDIO -> "オーディオエラー"
                            SpeechRecognizer.ERROR_CLIENT -> "クライアントエラー"
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "権限がありません"
                            SpeechRecognizer.ERROR_NETWORK -> "ネットワークエラー"
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "ネットワークタイムアウト"
                            SpeechRecognizer.ERROR_NO_MATCH -> "音声が認識されませんでした"
                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "認識エンジンがビジー状態"
                            SpeechRecognizer.ERROR_SERVER -> "サーバーエラー"
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "音声が検出されませんでした"
                            else -> "不明なエラー"
                        }
                        Log.e("SpeechToText", "音声認識エラー: $errorMessage ($error)")
                        Toast.makeText(context, "音声認識エラー: $errorMessage", Toast.LENGTH_SHORT).show()
                    }

                    override fun onEndOfSpeech() {
                        Log.d("SpeechToText", "音声入力が終了")
                        isListening = false
                    }

                    override fun onBeginningOfSpeech() {
                        Log.d("SpeechToText", "音声入力が開始")
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        matches?.firstOrNull()?.let { result ->
                            Log.d("SpeechToText", "途中結果: $result")
                            Toast.makeText(context, "途中結果: $result", Toast.LENGTH_SHORT).show()
                            onResult(result) // 🔹 `TextField` に即反映
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onRmsChanged(rmsdB: Float) {}
                })
            }
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja-JP") // 日本語
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        }

        Log.d("SpeechToText", "音声認識を開始")
        Toast.makeText(context, "マイクが起動しました", Toast.LENGTH_SHORT).show()
        speechRecognizer?.startListening(intent)
        isListening = true
    }

    fun checkPermissionAndStart() {
        val permission = Manifest.permission.RECORD_AUDIO
        val hasPermission = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            permissionLauncher.launch(permission)
            return
        }

        startListening()
    }

    Icon(
        imageVector = Icons.Default.Mic,
        contentDescription = "音声入力",
        modifier = Modifier
            .clickable {
                Log.d("SpeechToText", "マイクボタンが押された")
                Toast.makeText(context, "マイクを起動します...", Toast.LENGTH_SHORT).show()
                checkPermissionAndStart()
            }
            .size(40.dp)
    )
}

