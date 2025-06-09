package com.example.mastermind.data.dao

import androidx.room.*
import com.example.mastermind.data.entity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: GameEntity): Long

    @Query("SELECT * FROM game WHERE ongoing = 1 LIMIT 1")
    suspend fun getOngoing(): GameEntity?

    @Query("UPDATE game SET ongoing = 0 WHERE id = :id")
    suspend fun markFinished(id: Long)

    /* --------  Storico  -------- */

    @Query("SELECT * FROM game WHERE ongoing = 0 ORDER BY date DESC")
    fun getAllFinished(): Flow<List<GameEntity>>

    @Query("SELECT * FROM game WHERE id = :id")
    fun getById(id: Long): Flow<GameEntity?>

    @Query("SELECT * FROM game WHERE id = :id")
    suspend fun getByIdOnce(id: Long): GameEntity?

}


