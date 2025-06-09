package com.example.mastermind.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermind.data.MastermindDatabase
import com.example.mastermind.data.entity.GameEntity
import com.example.mastermind.data.entity.Move
import com.example.mastermind.data.preferences.PreferencesManager
import com.example.mastermind.data.repository.GameRepository
import com.example.mastermind.domain.logic.MastermindSolver
import com.example.mastermind.domain.model.ColorPeg
import com.example.mastermind.domain.model.GameSettings
import com.example.mastermind.domain.model.GameStatus
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/* ---------- UI-model (solo per la schermata) ---------- */
data class MoveResult(
    val guess:  List<ColorPeg>,
    val blacks: Int,
    val whites: Int
)

/* ---------- ViewModel ---------- */
class GameViewModel(app: Application) : AndroidViewModel(app) {

    /* ------------------------------------------------------------------ */
    private val repo = GameRepository(MastermindDatabase.get(app).gameDao())
    private val pref = PreferencesManager(app)
    private val gson = Gson()

    /* ------------------ Impostazioni reattive ------------------------- */
    private val _settings = MutableStateFlow(GameSettings())
    val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    /* ------------------ Stato partita -------------------------------- */
    private var secret: List<ColorPeg> = emptyList()
    private var gameId: Long? = null
    private val MAX_ATTEMPTS = 9

    private val _status = MutableStateFlow<GameStatus>(GameStatus.Playing)
    val status: StateFlow<GameStatus> = _status

    private val _moves = MutableStateFlow<List<MoveResult>>(emptyList())
    val moves: StateFlow<List<MoveResult>> = _moves

    private val _remaining = MutableStateFlow<Int?>(null)
    val remaining: StateFlow<Int?> = _remaining

    /* ------------------ Editor corrente ------------------------------ */
    private val _editing = MutableStateFlow<List<ColorPeg?>>(emptyList())
    val editing: StateFlow<List<ColorPeg?>> = _editing

    val canSubmit: StateFlow<Boolean> = combine(_editing, _settings) { g, s ->
        g.size == s.codeLength &&
                g.none { it == null } &&
                (s.allowDuplicates || g.filterNotNull().toSet().size == g.size)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    /* ------------------ Inizializzazione ------------------------------ */
    init {
        viewModelScope.launch {
            var first = true
            pref.settings.collect { newPref ->
                _settings.value = newPref
                if (first) {
                    first = false
                    if (!loadOngoing()) startNewGame(newPref)
                }
            }
        }
    }

    /* ------------------ Nuova partita -------------------------------- */
    fun startNewGame(cfg: GameSettings) = viewModelScope.launch {
        pref.save(cfg)
        _settings.value = cfg
        startInternal(cfg)
    }

    /* ------------------ Salvataggio rapido --------------------------- */
    suspend fun getOngoingSummary() = repo.ongoing()
    fun discardOngoing(id: Long) = viewModelScope.launch { repo.markFinished(id) }

    /* ------------------ Logica interna -------------------------------- */
    private suspend fun startInternal(cfg: GameSettings) {
        gameId?.let { repo.markFinished(it) }
        _status.value = GameStatus.Playing

        val palette = ColorPeg.subset(cfg.colors)
        secret = List(cfg.codeLength) {
            var next: ColorPeg
            do { next = palette.random() }
            while (!cfg.allowDuplicates && secret.contains(next))
            next
        }

        _moves.value     = emptyList()
        _editing.value   = List(cfg.codeLength) { null }
        _remaining.value = null

        gameId = repo.save(
            GameEntity(
                id           = 0,
                date         = LocalDateTime.now(),
                secret       = secret,
                moves        = emptyList(),
                settingsJson = gson.toJson(cfg),
                ongoing      = true
            )
        )
    }

    fun abandonWithoutSaving() = viewModelScope.launch {
        gameId?.let { repo.markFinished(it) }
    }

    /* ------------------ Editor --------------------------------------- */
    fun updatePeg(index: Int, color: ColorPeg?) =
        _editing.update { it.toMutableList().also { l -> l[index] = color } }

    /* ------------------ Commit tentativo ----------------------------- */
    @RequiresApi(Build.VERSION_CODES.O)
    fun commitGuess() {
        val guess = _editing.value.filterNotNull()
        val s = _settings.value
        if (guess.size != s.codeLength) return

        viewModelScope.launch {
            val (b, w) = MastermindSolver.feedback(secret, guess)
            _moves.value += MoveResult(guess, b, w)

            /* Ricalcola compatibili */
            _remaining.value = MastermindSolver.remainingCompatible(
                s,
                _moves.value.map { it.guess to (it.blacks to it.whites) }
            )

            _editing.value = List(s.codeLength) { null }

            saveProgress()   // autosave

            when {
                b == secret.size -> {
                    repo.markFinished(gameId!!)
                    _status.value = GameStatus.Won
                }
                _moves.value.size >= MAX_ATTEMPTS -> {
                    repo.markFinished(gameId!!)
                    _status.value = GameStatus.Lost(secret)
                }
            }
        }
    }

    /* ------------------ Salvataggio ---------------------------------- */
    fun saveProgress() = viewModelScope.launch {
        val idPrev = gameId ?: 0
        gameId = repo.save(
            GameEntity(
                id           = idPrev,
                date         = LocalDateTime.now(),
                secret       = secret,
                moves        = _moves.value.map { Move(it.guess, it.blacks, it.whites) },
                settingsJson = gson.toJson(_settings.value),
                ongoing      = true
            )
        )
    }

    /* ------------------ Caricamento ---------------------------------- */
    suspend fun loadOngoing(): Boolean {
        val g = repo.ongoing() ?: return false
        gameId         = g.id
        secret         = g.secret
        _settings.value = gson.fromJson(g.settingsJson, GameSettings::class.java)

        _moves.value = g.moves.map { MoveResult(it.guess, it.blacks, it.whites) }
        _remaining.value = MastermindSolver.remainingCompatible(
            _settings.value,
            _moves.value.map { it.guess to (it.blacks to it.whites) }
        )
        _editing.value = List(_settings.value.codeLength) { null }
        _status.value  = GameStatus.Playing
        return true
    }
}




