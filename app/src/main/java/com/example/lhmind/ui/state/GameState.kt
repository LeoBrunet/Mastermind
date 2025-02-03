package com.example.lhmind.ui.state

import com.example.lhmind.domain.model.Attempt
import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.model.Game

sealed class GameState {
    data object Initial : GameState()
    data class GameCreated(val game: Game) : GameState()
    data class AttemptMade(val attempt: Attempt) : GameState()
    data class FeedbackProvided(val feedback: Feedback) : GameState()
    data class Error(val message: String) : GameState()
}