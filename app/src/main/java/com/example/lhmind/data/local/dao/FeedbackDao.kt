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
    suspend fun getFeedbackByAttemptId(attemptId: Long): FeedbackEntity?
}