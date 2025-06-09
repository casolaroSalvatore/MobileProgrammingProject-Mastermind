package com.example.mastermind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.entity.GameEntity
import com.example.mastermind.data.repository.GameRepository
import com.example.mastermind.domain.model.ColorPeg
import kotlinx.coroutines.flow.*
import java.time.format.DateTimeFormatter

data class MoveFeedback(
    val guess: List<ColorPeg>,
    val blacks: Int,
    val whites: Int
)

class HistoryDetailViewModel(
    private val repo: GameRepository,
    private val gameId: Long
) : ViewModel() {

    private val _game: Flow<GameEntity?> = repo.gameById(gameId)

    val rows = _game
        .map { it?.moves?.map { m -> MoveFeedback(m.guess, m.blacks, m.whites) } ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val secret = _game
        .map { it?.secret ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val won = _game
        .map { it?.moves?.lastOrNull()?.let { m -> m.guess == it.secret } ?: false }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val formattedDate = _game
        .map { it?.date?.format(DateTimeFormatter.ofPattern("dd MMM yyyy – HH:mm")) ?: "" }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
}


