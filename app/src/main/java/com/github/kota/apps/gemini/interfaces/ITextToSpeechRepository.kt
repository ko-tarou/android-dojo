package com.github.kota.apps.gemini.interfaces

interface ITextToSpeechRepository {
    fun speak(text: String)

    fun stop()

    fun shutdown()
}