package com.example.mastermind.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.mastermind.domain.model.GameSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class PreferencesManager(private val context: Context) {

    private object Keys {
        val COLORS      = intPreferencesKey("colors")
        val LENGTH      = intPreferencesKey("length")
        val DUPLICATES  = booleanPreferencesKey("duplicates")
        val ATTEMPTS    = intPreferencesKey("attempts")

    }

    val settings: Flow<GameSettings> = context.dataStore.data.map { p ->
        GameSettings(
            p[Keys.COLORS] ?: 6,
            p[Keys.LENGTH] ?: 4,
            p[Keys.DUPLICATES] ?: false,
            p[Keys.ATTEMPTS] ?: 9
        )
    }

    suspend fun save(g: GameSettings) = context.dataStore.edit { p ->
        p[Keys.COLORS] = g.colors
        p[Keys.LENGTH] = g.codeLength
        p[Keys.DUPLICATES] = g.allowDuplicates
        p[Keys.ATTEMPTS] = g.maxAttempts
    }

    // Funzione per leggere un float
    fun floatFlow(keyName: String, default: Float): Flow<Float> {
        val key = floatPreferencesKey(keyName)
        return context.dataStore.data.map { prefs ->
            prefs[key] ?: default
        }
    }

    // Funzione per salvare un float
    suspend fun setFloat(keyName: String, value: Float) {
        val key = floatPreferencesKey(keyName)
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
}
