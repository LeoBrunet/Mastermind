package com.example.lhmind.domain.repository

import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.model.Player

interface PlayerRepository {
    suspend fun getPlayer(name: String): Player
    suspend fun registerPlayer(name: String): Long
    suspend fun getActiveGamesForPlayer(playerId: Long): List<Game>
    suspend fun getPlayerById(id: Long): Player
}