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
    }

    val settings: Flow<GameSettings> = context.dataStore.data.map { p ->
        GameSettings(
            p[Keys.COLORS] ?: 6,
            p[Keys.LENGTH] ?: 4,
            p[Keys.DUPLICATES] ?: false
        )
    }

    suspend fun save(g: GameSettings) = context.dataStore.edit { p ->
        p[Keys.COLORS] = g.colors
        p[Keys.LENGTH] = g.codeLength
        p[Keys.DUPLICATES] = g.allowDuplicates
    }
}
