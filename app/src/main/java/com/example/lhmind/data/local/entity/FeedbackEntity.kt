package com.example.lhmind.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "feedbacks")
data class FeedbackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val attemptId: Long,
    val correctPosition: Int,
    val correctColor: Int,
    val computerCorrectPosition : Int,
    val computerCorrectColor : Int,
    val feedbackTime: LocalDateTime
)