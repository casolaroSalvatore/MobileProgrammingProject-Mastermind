package com.example.mastermind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.preferences.PreferencesManager
import com.example.mastermind.domain.model.GameSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/*
 * ViewModel dello screen Impostazioni.
 * Espone lo StateFlow delle preferenze e metodi di modifica. */
class SettingViewModel(app: Application) : AndroidViewModel(app) {

    private val pref = PreferencesManager(app)

    /* Flusso delle impostazioni – reattivo grazie a DataStore. */
    val settings = pref.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GameSettings())

    /* ---------- metodi di modifica ---------- */

    fun setColors(n: Int) = update { copy(colors = n) }

    fun setLength(n: Int) = update { copy(codeLength = n) }

    fun setDuplicates(enabled: Boolean) = update { copy(allowDuplicates = enabled) }

    /* ---------- helper ---------- */

    private fun update(block: GameSettings.() -> GameSettings) = viewModelScope.launch {
        pref.save(settings.value.block())
    }
}

