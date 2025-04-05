package com.example.lhmind.data.local.dao

import androidx.room.*
import com.example.lhmind.data.local.entity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Insert
    suspend fun insert(game: GameEntity): Long

    @Query("SELECT * FROM games WHERE id = :gameId")
    suspend fun getGameById(gameId: Long): GameEntity?

    @Query("""
        SELECT * FROM games 
        WHERE (makerId = :playerId OR breakerId = :playerId) 
        AND status != 'COMPLETED'
        ORDER BY startTime DESC
    """)
    fun getActiveGamesForPlayer(playerId: Long): List<GameEntity>

    @Update
    suspend fun update(game: GameEntity)

    @Query("UPDATE games SET remainingAttempts = remainingAttempts - 1 WHERE id = :gameId")
    suspend fun decrementRemainingAttempts(gameId: Long)

    /*@Transaction
    @Query("""
        SELECT g.*, 
            (SELECT COUNT(*) FROM attempts a WHERE a.gameId = g.id) as attemptCount,
            (SELECT MAX(f.correctPosition) FROM feedbacks f
             INNER JOIN attempts a ON f.attemptId = a.id 
             WHERE a.gameId = g.id) as bestAttempt
        FROM games g
        WHERE g.id = :gameId
    """)
    suspend fun getGameWithStats(gameId: String): GameWithStats*/
}