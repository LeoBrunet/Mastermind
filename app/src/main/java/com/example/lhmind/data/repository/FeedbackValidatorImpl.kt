package com.example.lhmind.data.repository

import com.example.lhmind.data.local.dao.AttemptDao
import com.example.lhmind.data.local.dao.GameDao
import com.example.lhmind.data.mapper.GameMapper
import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.model.GameStatus
import com.example.lhmind.domain.repository.FeedbackValidator
import com.example.lhmind.domain.repository.GameRepository
import javax.inject.Inject

class FeedbackValidatorImpl @Inject constructor(
    private val gameRepository: GameRepository,
    private val attemptDao: AttemptDao,
    private val gameDao: GameDao,
    private val gameMapper: GameMapper
) : FeedbackValidator {

    override suspend fun validate(feedback: Feedback) : Boolean {
        val finalFeedback = calculateFeedback(feedback)

        return (finalFeedback.correctPosition == feedback.correctPosition) && (finalFeedback.correctColor == feedback.correctColor)
    }

    override suspend fun validateAndSave(feedback: Feedback): Feedback {
        val attempt = attemptDao.getAttemptById(feedback.attemptId)
        val finalFeedback = calculateFeedback(feedback)

        val gameStatus = if (finalFeedback.computerCorrectPosition == 4 && finalFeedback.correctPosition == 4) {
            GameStatus.COMPLETED
        } else if ((finalFeedback.computerCorrectPosition != finalFeedback.correctPosition) || (finalFeedback.computerCorrectColor != finalFeedback.correctColor)) {
            GameStatus.WRONG_FEEDBACK
        } else {
            GameStatus.WAITING_FOR_ATTEMPT
        }

        gameDao.getGameById(attempt.gameId)?.copy(status = gameStatus)
            ?.let { gameDao.update(it) }

        return gameRepository.saveFeedback(finalFeedback)
    }

    private suspend fun calculateFeedback(feedback: Feedback) : Feedback {
        val attempt = attemptDao.getAttemptById(feedback.attemptId)
        val game = gameDao.getGameById(attempt.gameId)?.let { gameMapper.mapEntityToDomain(it) }

        val (correctPositions, correctColors) = game?.let {
            val guess = attempt.pegs
            val secret = game.secretCombination

            val colorCounts = secret.groupingBy { it.color }.eachCount().toMutableMap()
            var correctColors = 0

            for (peg in guess) {
                val count = colorCounts[peg.color] ?: 0
                if (count > 0) {
                    correctColors++
                    colorCounts[peg.color] = count - 1
                }
            }

            println(secret)
            println(guess)

            secret.zip(guess).count { (a, b) -> a == b } to correctColors
        } ?: (0 to 0)

        println("Correct positions: $correctPositions, correct colors: $correctColors")

        return feedback.copy(
            computerCorrectPosition = correctPositions,
            computerCorrectColor = correctColors
        )
    }
}