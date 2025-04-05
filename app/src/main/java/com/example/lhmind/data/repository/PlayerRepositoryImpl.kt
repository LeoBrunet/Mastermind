package com.example.lhmind.data.repository

import com.example.lhmind.data.local.dao.PlayerDao
import com.example.lhmind.data.local.entity.PlayerEntity
import com.example.lhmind.data.mapper.GameMapper
import com.example.lhmind.data.mapper.PlayerMapper
import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.model.Player
import com.example.lhmind.domain.repository.PlayerRepository
import java.time.LocalDateTime
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val playerDao: PlayerDao,
    private val playerMapper: PlayerMapper,
    private val gameMapper: GameMapper
) : PlayerRepository {
    
    override suspend fun getPlayer(name: String): Player {
        val playerEntity = playerDao.findByName(name) ?: throw IllegalArgumentException("Player not found")
        return playerMapper.mapEntityToDomain(playerEntity)
    }

    override suspend fun getPlayerById(id: Long): Player {
        val playerEntity = playerDao.findById(id) ?: throw IllegalArgumentException("Player not found")
        return playerMapper.mapEntityToDomain(playerEntity)
    }

    override suspend fun registerPlayer(name: String): Long {
        val existingPlayer = playerDao.findByName(name)
        return if (existingPlayer != null) {
            existingPlayer.id
        } else {
            val playerEntity = PlayerEntity(
                name = name,
                registrationDate = LocalDateTime.now()
            )
            playerDao.insert(playerEntity)
        }
    }

    override suspend fun getActiveGamesForPlayer(playerId: Long): List<Game> {
        return playerDao.getActiveGamesForPlayer(playerId).map { gameMapper.mapEntityToDomain(it) }
    }
}