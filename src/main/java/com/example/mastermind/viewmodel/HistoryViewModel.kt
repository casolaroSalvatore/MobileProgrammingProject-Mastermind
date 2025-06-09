package com.example.mastermind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.MastermindDatabase
import com.example.mastermind.data.repository.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

/* Espone le partite concluse con tentativi e vittoria/sconfitta. */
class HistoryViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = GameRepository(MastermindDatabase.get(app).gameDao())

    data class HistoryItem(
        val id: Long,
        val date: LocalDateTime,
        val attempts: Int,
        val won: Boolean
    )

    val games = repo.finished()     // Flow<List<GameEntity>>
        .map { list ->
            list.map { g ->
                val victory = g.moves.lastOrNull()?.guess?.let { it == g.secret } ?: false
                HistoryItem(
                    id        = g.id,
                    date      = g.date,
                    attempts  = g.moves.size,
                    won       = victory
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}



