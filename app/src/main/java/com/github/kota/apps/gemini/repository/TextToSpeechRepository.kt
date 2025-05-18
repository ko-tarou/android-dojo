package com.github.kota.apps.gemini.repository

import android.content.Context
import android.speech.tts.TextToSpeech
import com.github.kota.apps.gemini.interfaces.ITextToSpeechRepository
import java.util.*
import javax.inject.Inject

class TextToSpeechRepository @Inject constructor(
    context: Context
) : TextToSpeech.OnInitListener,
    ITextToSpeechRepository {

    private var tts: TextToSpeech = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.JAPANESE // 🔹 日本語を設定
        }
    }

    override fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun stop() {
        tts.stop()
    }

    override fun shutdown() {
        tts.shutdown()
    }
}
