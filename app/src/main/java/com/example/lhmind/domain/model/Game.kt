package com.example.lhmind.domain.model

import java.time.LocalDateTime

data class Game(
    val id: Long = 0,
    val makerId: Long,
    val breakerId: Long,
    val creatorIsMaker: Boolean,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime? = null,
    val remainingAttempts: Int = 12,
    val status: GameStatus,
    val secretCombination: List<Peg>
)