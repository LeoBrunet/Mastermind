package com.example.lhmind.domain.model

import java.time.LocalDateTime

data class Player(
    val id: Long = 0,
    val name: String,
    val registrationDate: LocalDateTime
)