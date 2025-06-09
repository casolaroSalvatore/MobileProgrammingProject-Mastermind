package com.example.mastermind.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mastermind.domain.model.ColorPeg
import com.example.mastermind.data.util.Converters
import java.time.LocalDateTime

@Entity(tableName = "game")
@TypeConverters(Converters::class)
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDateTime,
    val secret: List<ColorPeg>,
    val moves: List<Move>,
    val settingsJson: String,
    val ongoing: Boolean = true
)


