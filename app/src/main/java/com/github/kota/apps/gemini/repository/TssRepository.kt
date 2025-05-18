package com.github.kota.apps.gemini.repository

import com.github.kota.apps.gemini.data.FontSizeDataStore
import com.github.kota.apps.gemini.data.TssDataStore
import com.github.kota.apps.gemini.interfaces.ITssRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TssRepository @Inject constructor(
    private val dataStore: TssDataStore
) : ITssRepository {
    override fun getIsTss(): Flow<Boolean> = dataStore.isTss

    override suspend fun saveIsTss(check: Boolean) {
        dataStore.saveIsTss(check)
    }
}
