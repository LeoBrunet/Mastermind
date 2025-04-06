package com.example.lhmind.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lhmind.data.local.entity.AttemptEntity

@Dao
interface AttemptDao {
    @Insert
    suspend fun insert(attempt: AttemptEntity): Long

    @Query("SELECT * FROM attempts WHERE id == :attemptId")
    suspend fun getAttemptById(attemptId: Long): AttemptEntity

    @Query("SELECT COUNT(*) FROM attempts WHERE gameId == :gameId")
    suspend fun getNumberOfAttemptForGame(gameId: Long): Int

    @Query("SELECT * FROM attempts WHERE gameId == :gameId")
    suspend fun getAttemptsForGame(gameId: Long): List<AttemptEntity>
}