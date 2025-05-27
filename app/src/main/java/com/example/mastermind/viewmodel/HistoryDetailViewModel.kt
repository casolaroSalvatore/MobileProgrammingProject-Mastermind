package com.example.mastermind.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.mastermind.data.MastermindDatabase
import com.example.mastermind.data.repository.GameRepository
import com.example.mastermind.domain.logic.MastermindSolver
import com.example.mastermind.domain.model.ColorPeg
import com.example.mastermind.domain.model.GameSettings
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class DetailRow(
    val guess: List<ColorPeg>,
    val blacks: Int,
    val whites: Int
)

/* ViewModel parametrizzato con l’id della partita da visualizzare. */
class HistoryDetailViewModel(
    app: Application,
    private val gameId: Long
) : AndroidViewModel(app) {

    private val repo = GameRepository(MastermindDatabase.get(app).gameDao())
    private val gson = Gson()

    private val _date      = MutableStateFlow<LocalDateTime?>(null)
    val date: StateFlow<LocalDateTime?> = _date

    private val _settings  = MutableStateFlow<GameSettings?>(null)
    val settings: StateFlow<GameSettings?> = _settings

    private val _rows      = MutableStateFlow<List<DetailRow>>(emptyList())
    val rows: StateFlow<List<DetailRow>> = _rows

    init {
        viewModelScope.launch {
            repo.game(gameId)?.let { game ->
                _date.value     = game.date
                _settings.value = gson.fromJson(game.settingsJson, GameSettings::class.java)

                val secret = game.secret
                _rows.value = game.moves.map { guess ->
                    val (b, w) = MastermindSolver.feedback(secret, guess)
                    DetailRow(guess, b, w)
                }
            }
        }
    }

    /* Factory per passare gameId dal NavGraph */
    class Factory(
        private val app: Application,
        private val id: Long
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HistoryDetailViewModel(app, id) as T
    }
}
