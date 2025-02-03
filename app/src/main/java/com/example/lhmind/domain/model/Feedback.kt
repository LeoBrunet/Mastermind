package com.example.lhmind.domain.model

import java.time.LocalDateTime

data class Feedback(
    val id: Long = 0,
    val attemptId: Long,
    val correctPosition: Int,
    val correctColor: Int,
    val computerCorrectPosition : Int = -1,
    val computerCorrectColor : Int = -1,
    val feedbackTime: LocalDateTime
)