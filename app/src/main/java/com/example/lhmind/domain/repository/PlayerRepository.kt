package com.example.lhmind.domain.repository

import com.example.lhmind.domain.model.Game

interface PlayerRepository {
    suspend fun getActiveGamesForPlayer(playerId: Long): List<Game>
}