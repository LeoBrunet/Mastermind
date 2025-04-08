package com.example.lhmind.domain.usecase

import com.example.lhmind.domain.model.Attempt
import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.model.Peg
import com.example.lhmind.domain.model.Player
import com.example.lhmind.domain.repository.FeedbackValidator
import com.example.lhmind.domain.repository.GameRepository
import com.example.lhmind.domain.repository.PlayerRepository
import javax.inject.Inject

class GameUseCase @Inject constructor(
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val feedbackValidator: FeedbackValidator
) {
    suspend fun createGame(makerId: Long, breakerId: Long, creatorIsMaker: Boolean): Game {
        return gameRepository.createGame(makerId, breakerId, creatorIsMaker)
    }

    suspend fun makeAttempt(gameId: Long, pegs: List<Peg>): Attempt {
        return gameRepository.makeAttempt(gameId, pegs)
    }

    suspend fun getAttempts(gameId: Long): List<Attempt> {
        return gameRepository.getAttempts(gameId)
    }

    suspend fun provideFeedback(
        feedback: Feedback
    ): Feedback {
        return feedbackValidator.validateAndSave(feedback)
    }

    suspend fun validateFeedback(feedback: Feedback) : Boolean {
        return feedbackValidator.validate(feedback)
    }

    suspend fun getGame(gameId: Long): Game {
        return gameRepository.getGame(gameId)
    }

    suspend fun getPlayerById(playerId: Long) : Player {
        return playerRepository.getPlayerById(playerId)
    }

    suspend fun getPlayer(playerName: String) : Player {
        return playerRepository.getPlayer(playerName)
    }

    suspend fun createSecretCombination(gameId: Long, pegs: List<Peg>) {
        gameRepository.createSecretCombination(gameId, pegs)
    }
}