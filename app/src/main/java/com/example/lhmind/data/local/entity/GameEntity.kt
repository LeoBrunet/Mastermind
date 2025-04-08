package com.example.lhmind.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lhmind.domain.model.GameStatus
import com.example.lhmind.domain.model.Peg
import java.time.LocalDateTime

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val makerId: Long,
    val breakerId: Long,
    val creatorIsMaker: Boolean,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val remainingAttempts: Int,
    val status: GameStatus = GameStatus.INVITATION_SENT,
    val secretCombination: List<Peg> = emptyList()
)