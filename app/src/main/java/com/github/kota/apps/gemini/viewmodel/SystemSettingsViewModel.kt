package com.github.kota.apps.gemini.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.kota.apps.gemini.data.FontSizeDataStore
import com.github.kota.apps.gemini.data.TssDataStore
import com.github.kota.apps.gemini.repository.FontSizeRepository
import com.github.kota.apps.gemini.repository.TssRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SystemSettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val fontSizeRepository = FontSizeRepository(FontSizeDataStore(application))
    private val tssRepository = TssRepository(TssDataStore(application))

    private val _isTss = MutableStateFlow(false)
    val isTss: StateFlow<Boolean> = _isTss

    private val _fontSize = MutableStateFlow(16)
    val fontSize: StateFlow<Int> = _fontSize

    init {
        loadFontSize()
        loadIsTss()
    }

    private fun loadFontSize() {
        viewModelScope.launch {
            fontSizeRepository.getFontSize().collect { size ->
                _fontSize.value = size
            }
        }
    }

    fun updateFontSize(size: Int) {
        viewModelScope.launch {
            fontSizeRepository.saveFontSize(size)
            _fontSize.value = size
        }
    }

    private fun loadIsTss() {
        viewModelScope.launch {
            tssRepository.getIsTss().collect { check ->
                _isTss.value = check
            }
        }
    }

    fun updateIsTss(check: Boolean) {
        viewModelScope.launch {
            tssRepository.saveIsTss(check)
            _isTss.value = check
        }
    }

}
