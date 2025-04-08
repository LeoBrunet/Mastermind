package com.example.lhmind.domain.model

import java.time.LocalDateTime

data class Invitation(
    val gameId: Long,
    val playerNameSender: String,
    val playerNameReceiver: String,
    val senderIsMaker: Boolean,
    val startTime: LocalDateTime
)