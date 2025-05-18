package com.github.kota.apps.gemini.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kota.apps.gemini.repository.GeminiRepository
import com.github.kota.apps.gemini.model.Message
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.github.kota.apps.gemini.data.MessageDatabase
import com.github.kota.apps.gemini.repository.ChatRepository
import com.github.kota.apps.gemini.data.SelectedApiKeyDataStore
import com.github.kota.apps.gemini.data.TssDataStore
import com.github.kota.apps.gemini.interfaces.IChatRepository
import com.github.kota.apps.gemini.interfaces.IGeminiRepository
import com.github.kota.apps.gemini.interfaces.ISelectedApiKeyRepository
import com.github.kota.apps.gemini.interfaces.ITextToSpeechRepository
import com.github.kota.apps.gemini.interfaces.ITssRepository
import com.github.kota.apps.gemini.repository.SelectedApiKeyRepository
import com.github.kota.apps.gemini.repository.TextToSpeechRepository
import com.github.kota.apps.gemini.repository.TssRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: IChatRepository,
    private val apiRepository : IGeminiRepository,
    private val selectedApiKeyRepository : ISelectedApiKeyRepository,
    private val textToSpeechRepository : ITextToSpeechRepository,
    private val tssRepository : ITssRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _selectedChatName = MutableStateFlow<String>("kota")
    val selectedChatName: StateFlow<String> = _selectedChatName

    private val _selectedApiKey = MutableStateFlow<String?>(null)
    val selectedApiKey: StateFlow<String?> = _selectedApiKey

    private val _isTssEnabled = MutableStateFlow<Boolean>(false)
    val isTssEnabled: StateFlow<Boolean> = _isTssEnabled

    private val _chatNames = MutableStateFlow<List<String>>(emptyList())
    val chatNames: StateFlow<List<String>> = _chatNames

    init {
        loadMessagesForChat(_selectedChatName.value)
        loadSelectedApiKey()
        loadChatNames()
        loadTssState()
    }

    private fun loadTssState(){
        viewModelScope.launch {
            tssRepository.getIsTss().collect { state ->
                _isTssEnabled.value = state
            }
        }
    }

    fun updateTssState(isEnabled: Boolean){
        viewModelScope.launch {
            tssRepository.saveIsTss(isEnabled)
            _isTssEnabled.value = isEnabled
        }
    }

    // 🔹 チャットを変更したら、そのチャットのメッセージをロード
    fun selectChat(chatName: String) {
        _selectedChatName.value = chatName
        _messages.value = emptyList()
        loadMessagesForChat(chatName)
    }

    // 🔹 選択中の API キーを取得
    private fun loadSelectedApiKey() {
        viewModelScope.launch {
            selectedApiKeyRepository.getSelectedApiKey().collect { key ->
                _selectedApiKey.value = key
            }
        }
    }

    // 🔹 特定の `chatName` のメッセージをロードする関数
    private fun loadMessagesForChat(chatName: String) {
        viewModelScope.launch {
            messageRepository.getMessagesByChatName(chatName).collectLatest { messages ->
                _messages.value = messages
            }
        }
    }

    fun deleteMessagesForChat(chatName: String) {
        viewModelScope.launch {
            messageRepository.deleteMessagesByChatName(chatName)
        }
    }

    private fun loadChatNames() {
        viewModelScope.launch {
            messageRepository.getUniqueChatNames().collect { names ->
                _chatNames.value = names
            }
        }
    }

    fun addChatName(chatName: String) {
        viewModelScope.launch {
            if (!_chatNames.value.contains(chatName)) {
                _chatNames.value = _chatNames.value + chatName
            }
        }
    }

    fun sendMessage(userMessage: String?, imageUri: Uri?) {
        if (userMessage.isNullOrBlank() && imageUri == null) return

        val chatName = selectedChatName.value
        val newUserMessage = Message(chatName, userMessage, true, imageUri)

        viewModelScope.launch {
            messageRepository.insertMessage(newUserMessage)

            if (_selectedChatName.value == chatName) {
                _messages.value = _messages.value + newUserMessage
            }

            val apiKey = _selectedApiKey.value ?: return@launch // 🔹 APIキーが null ならリクエストしない
            val response = apiRepository.getGeminiResponse(userMessage, imageUri, apiKey)
            val aiMessage = Message(chatName, response, false)
            _messages.value = _messages.value + aiMessage

            messageRepository.insertMessage(aiMessage)

            if(_isTssEnabled.value){
                textToSpeechRepository.speak(response)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeechRepository.shutdown()
    }
}
