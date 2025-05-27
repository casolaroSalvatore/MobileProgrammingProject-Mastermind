package com.example.mastermind.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mastermind.data.util.Converters
import com.example.mastermind.domain.model.ColorPeg
import java.time.LocalDateTime

@Entity(tableName = "games")
@TypeConverters(Converters::class)
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDateTime,                 // Tempo di creazione della partita
    val secret: List<ColorPeg>,              // Codice da indovinare
    val moves: List<List<ColorPeg>>,         // Tutti i tentativi eseguiti
    val settingsJson: String,                // GameSettings serializzato
    val ongoing: Boolean = true              // Se false --> partita conclusa
)

