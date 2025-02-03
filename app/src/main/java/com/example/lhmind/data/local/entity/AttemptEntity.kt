package com.example.lhmind.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lhmind.domain.model.Peg
import java.time.LocalDateTime

@Entity(tableName = "attempts")
data class AttemptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameId: Long,
    val attemptNumber: Int,
    val attemptTime: LocalDateTime,
    val pegs: List<Peg>
)