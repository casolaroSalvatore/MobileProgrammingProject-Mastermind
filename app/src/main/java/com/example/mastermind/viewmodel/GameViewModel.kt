package com.example.mastermind.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.MastermindDatabase
import com.example.mastermind.data.entity.GameEntity
import com.example.mastermind.data.preferences.PreferencesManager
import com.example.mastermind.data.repository.GameRepository
import com.example.mastermind.domain.logic.MastermindSolver
import com.example.mastermind.domain.model.ColorPeg
import com.example.mastermind.domain.model.GameSettings
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/* ---------- UI-model ---------- */
data class MoveResult(val guess: List<ColorPeg>, val blacks: Int, val whites: Int)

/* ---------- ViewModel ---------- */
class GameViewModel(app: Application) : AndroidViewModel(app) {

    /* Dipendenze -------------------------------------------------------- */
    private val repo  = GameRepository(MastermindDatabase.get(app).gameDao())
    private val pref  = PreferencesManager(app)
    private val gson  = Gson()

    /* Impostazioni reattive -------------------------------------------- */
    private val _settings = MutableStateFlow(GameSettings())
    val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    /* Stato partita ----------------------------------------------------- */
    private var secret: List<ColorPeg> = emptyList()
    private var gameId: Long? = null

    private val _moves     = MutableStateFlow<List<MoveResult>>(emptyList())
    val moves: StateFlow<List<MoveResult>> = _moves

    private val _remaining = MutableStateFlow<Int?>(null)
    val remaining: StateFlow<Int?> = _remaining

    /* Editor ------------------------------------------------------------ */
    private val _editing  = MutableStateFlow<List<ColorPeg?>>(emptyList())
    val editing: StateFlow<List<ColorPeg?>> = _editing

    val canSubmit: StateFlow<Boolean> = combine(_editing, _settings) { g, s ->
        g.size == s.codeLength &&
                g.none { it == null } &&
                (s.allowDuplicates || g.filterNotNull().toSet().size == g.size)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    /*  Inizializzazione: preferenze + carica partita salvata oppure creane una nuova */
    init {
        viewModelScope.launch {
            var first = true
            pref.settings.collect { newPref ->
                _settings.value = newPref
                if (first) {
                    first = false
                    /* se esiste una partita ongoing, la carico;
                       altrimenti ne creo una nuova */
                    if (!loadOngoing()) startNewGame()
                }
            }
        }
    }

    /* Nuova partita   */
    fun startNewGame() = viewModelScope.launch {
        gameId?.let { repo.markFinished(it) }          // chiude la precedente

        val s = _settings.value
        val palette = ColorPeg.subset(s.colors)
        secret = List(s.codeLength) { palette.random() }
            .let { if (s.allowDuplicates) it else it.distinct() }

        _moves.value     = emptyList()
        _remaining.value = null
        _editing.value   = List(s.codeLength) { null }

        gameId = repo.save(
            GameEntity(
                id = 0,
                date = LocalDateTime.now(),
                secret = secret,
                moves = emptyList(),
                settingsJson = gson.toJson(s),
                ongoing = true
            )
        )
    }

    /* Aggiorna editor */
    fun updatePeg(index: Int, color: ColorPeg?) =
        _editing.update { it.toMutableList().also { l -> l[index] = color } }

    /* Invia tentativo */
    @RequiresApi(Build.VERSION_CODES.O)
    fun commitGuess() {
        val guess = _editing.value.filterNotNull()
        val s = _settings.value
        if (guess.size != s.codeLength) return

        viewModelScope.launch {
            val (b, w) = MastermindSolver.feedback(secret, guess)
            _moves.value += MoveResult(guess, b, w)

            _remaining.value = MastermindSolver.remainingCompatible(
                s,
                _moves.value.map { it.guess to (it.blacks to it.whites) }
            )

            _editing.value = List(s.codeLength) { null }
            // Autosalvataggio
            saveProgress()

            // Vittoria
            if (b == secret.size) {
                gameId?.let { repo.markFinished(it) }
                gameId = null
            }
        }
    }

    /* Salvataggio */
    fun saveProgress() = viewModelScope.launch {
        val id = gameId ?: 0
        gameId = repo.save(
            GameEntity(
                id = id,
                date = LocalDateTime.now(),
                secret = secret,
                moves = _moves.value.map { it.guess },
                settingsJson = gson.toJson(_settings.value),
                ongoing = true
            )
        )
    }

    /* Caricamento partita in corso */
    suspend fun loadOngoing(): Boolean {
        val g = repo.ongoing() ?: return false
        gameId  = g.id
        secret  = g.secret
        _settings.value = gson.fromJson(g.settingsJson, GameSettings::class.java)

        _moves.value = g.moves.map {
            val (b, w) = MastermindSolver.feedback(secret, it)
            MoveResult(it, b, w)
        }
        _remaining.value = MastermindSolver.remainingCompatible(
            _settings.value,
            _moves.value.map { it.guess to (it.blacks to it.whites) }
        )
        _editing.value = List(_settings.value.codeLength) { null }
        return true
    }
}




