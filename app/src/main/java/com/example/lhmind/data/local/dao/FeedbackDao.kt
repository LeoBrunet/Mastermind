package com.example.lhmind.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lhmind.data.local.entity.FeedbackEntity

@Dao
interface FeedbackDao {
    @Insert
    suspend fun insert(feedback: FeedbackEntity): Long

    @Query("SELECT * FROM feedbacks WHERE attemptId = :attemptId")
    suspend fun getFeedbackByAttemptId(attemptId: Long): List<FeedbackEntity>

    // FOR STATS
    @Query("SELECT * FROM feedbacks f JOIN attempts a ON a.id = f.attemptId JOIN games g ON a.gameId = g.id WHERE g.makerId = :playerId AND (computerCorrectColor != correctColor OR computerCorrectPosition != correctPosition)")
    suspend fun getFalseFeedbacksForPlayerId(playerId: Long): List<FeedbackEntity>

    @Query("""
        SELECT f.* FROM feedbacks f
        INNER JOIN (
            SELECT f.attemptId, MAX(f.feedbackTime) AS latestTime
            FROM feedbacks f
            INNER JOIN attempts a ON f.attemptId = a.id
            WHERE a.gameId = :gameId
            GROUP BY f.attemptId
        ) latest
        ON f.attemptId = latest.attemptId AND f.feedbackTime = latest.latestTime
    """)
    suspend fun getLastFeedbacksForGame(gameId: Long): List<FeedbackEntity>
}