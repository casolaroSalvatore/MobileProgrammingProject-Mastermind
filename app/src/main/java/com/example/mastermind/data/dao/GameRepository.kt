package com.example.mastermind.data.repository

import com.example.mastermind.data.dao.GameDao
import com.example.mastermind.data.entity.GameEntity
import kotlinx.coroutines.flow.Flow

class GameRepository(private val dao: GameDao) {

    fun finished(): Flow<List<GameEntity>>      = dao.getAllFinished()
    suspend fun save(game: GameEntity): Long    = dao.insert(game)
    suspend fun ongoing(): GameEntity?          = dao.getOngoing()
    suspend fun markFinished(id: Long)          = dao.markFinished(id)
    suspend fun game(id: Long): GameEntity?     = dao.getById(id)
}

