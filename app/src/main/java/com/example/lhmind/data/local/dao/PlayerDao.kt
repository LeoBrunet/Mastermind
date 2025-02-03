package com.example.lhmind.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lhmind.data.local.entity.GameEntity
import com.example.lhmind.data.local.entity.PlayerEntity

@Dao
interface PlayerDao {
    @Insert
    suspend fun insert(player: PlayerEntity): Long

    @Query("SELECT * FROM players WHERE id = :playerId")
    suspend fun getPlayerById(playerId: Long): PlayerEntity

    @Query("SELECT * FROM games WHERE makerId = :playerId OR breakerId = :playerId")
    suspend fun getActiveGamesForPlayer(playerId: Long): List<GameEntity>
}