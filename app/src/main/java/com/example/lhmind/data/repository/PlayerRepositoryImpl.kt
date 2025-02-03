package com.example.lhmind.data.repository

import com.example.lhmind.data.local.dao.PlayerDao
import com.example.lhmind.data.mapper.GameMapper
import com.example.lhmind.data.mapper.PlayerMapper
import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.repository.PlayerRepository

class PlayerRepositoryImpl(
    private val playerDao: PlayerDao,
    private val playerMapper: PlayerMapper,
    private val gameMapper: GameMapper
) : PlayerRepository {

    override suspend fun getActiveGamesForPlayer(playerId: Long): List<Game> {
        val gamesEntities = playerDao.getActiveGamesForPlayer(playerId)
        return gamesEntities.map { gameMapper.mapEntityToDomain(it) }
    }
}