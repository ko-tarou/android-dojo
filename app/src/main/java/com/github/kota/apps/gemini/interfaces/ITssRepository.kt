package com.github.kota.apps.gemini.interfaces

import kotlinx.coroutines.flow.Flow

interface ITssRepository {
    fun getIsTss(): Flow<Boolean>

    suspend fun saveIsTss(check: Boolean)
}