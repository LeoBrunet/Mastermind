package com.example.lhmind.data.local.dao

import androidx.room.*
import com.example.lhmind.data.local.entity.GameEntity
import com.example.lhmind.data.local.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert
    suspend fun insert(player: PlayerEntity): Long

    @Query("SELECT * FROM players WHERE name = :name")
    suspend fun findByName(name: String): PlayerEntity?

    @Query("SELECT * FROM players WHERE id = :id")
    fun findById(id: Long): PlayerEntity?

    @Query("SELECT * FROM games WHERE makerId = :playerId OR breakerId = :playerId")
    fun getActiveGamesForPlayer(playerId: Long): List<GameEntity>

}