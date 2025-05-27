package com.example.mastermind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.MastermindDatabase
import com.example.mastermind.data.repository.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Espone lo stream delle partite concluse (ongoing = false)
 * già arricchite con il numero di tentativi.
 */
class HistoryViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = GameRepository(MastermindDatabase.get(app).gameDao())

    /** UI-model: data + numero tentativi ricavato da moves.size */
    data class HistoryItem(
        val id: Long,
        val date: java.time.LocalDateTime,
        val attempts: Int
    )

    val games = repo.finished()                 // Flow<List<GameEntity>>
        .map { list ->
            list.map { g -> HistoryItem(g.id, g.date, g.moves.size) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

