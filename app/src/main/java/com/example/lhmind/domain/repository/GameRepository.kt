package com.example.lhmind.domain.repository

import com.example.lhmind.domain.model.Attempt
import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.model.Peg

interface GameRepository {
    suspend fun createGame(makerId: Long, breakerId: Long): Game
    suspend fun getGame(gameId: Long): Game
    suspend fun makeAttempt(gameId: Long, pegs: List<Peg>): Attempt
    suspend fun saveFeedback(feedback: Feedback): Feedback
    suspend fun getActiveGamesForPlayer(playerId: Long): List<Game>
}