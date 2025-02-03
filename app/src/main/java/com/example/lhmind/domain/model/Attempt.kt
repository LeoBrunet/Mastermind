package com.example.lhmind.domain.model

import java.time.LocalDateTime

data class Attempt(
    val id: Long = 0,
    val gameId: Long,
    val attemptNumber: Int,
    val attemptTime: LocalDateTime,
    val pegs: List<Peg>
)