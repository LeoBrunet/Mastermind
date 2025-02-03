package com.example.lhmind.domain.usecase

import com.example.lhmind.domain.model.Attempt
import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.model.Peg
import com.example.lhmind.domain.repository.FeedbackValidator
import com.example.lhmind.domain.repository.GameRepository
import java.time.LocalDateTime

class GameUseCase(
    private val gameRepository: GameRepository,
    private val feedbackValidator: FeedbackValidator
) {
    suspend fun createGame(makerId: Long, breakerId: Long): Game {
        return gameRepository.createGame(makerId, breakerId)
    }

    suspend fun makeAttempt(gameId: Long, pegs: List<Peg>): Attempt {
        return gameRepository.makeAttempt(gameId, pegs)
    }

    suspend fun provideFeedback(
        attemptId: Long,
        correctPosition: Int,
        correctColor: Int
    ): Feedback {
        val feedback = Feedback(
            attemptId = attemptId,
            correctPosition = correctPosition,
            correctColor = correctColor,
            feedbackTime = LocalDateTime.now()
        )

        return feedbackValidator.validateAndSave(feedback)
    }
}