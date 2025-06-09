package com.example.mastermind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/* ViewModel dello screen Impostazioni. Espone lo StateFlow delle preferenze e metodi di modifica. */
class SettingViewModel(app: Application) : AndroidViewModel(app) {

    private val pref = PreferencesManager(app)

    // Volumi reattivi
    val musicVolume = pref.floatFlow("music_vol", 0.8f)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.8f)

    val sfxVolume = pref.floatFlow("sfx_vol", 1.0f)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 1.0f)

    fun setMusicVolume(v: Float) = viewModelScope.launch {
        pref.setFloat("music_vol", v.coerceIn(0f, 1f))
    }

    fun setSfxVolume(v: Float) = viewModelScope.launch {
        pref.setFloat("sfx_vol", v.coerceIn(0f, 1f))
    }
}



