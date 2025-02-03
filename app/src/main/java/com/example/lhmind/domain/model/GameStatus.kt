package com.example.lhmind.domain.model

enum class GameStatus {
    WAITING_FOR_ATTEMPT,
    WAITING_FOR_FEEDBACK,
    WRONG_FEEDBACK,
    COMPLETED,
    ABANDONED
}