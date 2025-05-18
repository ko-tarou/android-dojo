package com.github.kota.apps.gemini.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "Tss")


class TssDataStore(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val IS_TSS_KEY = booleanPreferencesKey("isTss")
        private val DEFAULT_IS_TSS = false
    }

    val isTss: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_TSS_KEY] ?: DEFAULT_IS_TSS
        }

    suspend fun saveIsTss(check: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_TSS_KEY] = check
        }
    }
}
