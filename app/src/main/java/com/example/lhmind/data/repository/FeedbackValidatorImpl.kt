package com.example.lhmind.data.repository

import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.model.GameStatus
import com.example.lhmind.domain.repository.FeedbackValidator
import com.example.lhmind.domain.repository.GameRepository
import com.example.lhmind.data.local.dao.AttemptDao
import com.example.lhmind.data.local.dao.GameDao
import com.example.lhmind.data.mapper.GameMapper
import javax.inject.Inject

class FeedbackValidatorImpl @Inject constructor(
    private val gameRepository: GameRepository,
    private val attemptDao: AttemptDao,
    private val gameDao: GameDao,
    private val gameMapper: GameMapper
) : FeedbackValidator {

    override suspend fun validate(feedback: Feedback) : Boolean {
        val finalFeedback = calculateFeedback(feedback)

        return (finalFeedback.correctPosition != feedback.correctPosition) || (finalFeedback.correctColor != feedback.correctColor)
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
        var correctColors = 0
        var correctPositions = 0

        gameDao.getGameById(attempt.gameId)?.let { gameMapper.mapEntityToDomain(it) }?.let { game ->
            val pegsToFind = game.secretCombination
            val pegsAttempt = attempt.pegs

            // TODO: CHECK IF THIS IS WORKING
            println("pegsToFind: $pegsToFind")

            pegsAttempt.forEach { pegAttempt ->
                println("pegAttempt: $pegAttempt")
                println("correctColor: ${pegsToFind.map { it.color }.contains(pegAttempt.color)}")
                println("correctPositions: ${pegsToFind.contains(pegAttempt)}")
                if (pegsToFind.map { it.color }.contains(pegAttempt.color)) {
                    correctColors += 1
                }
                if (pegsToFind.contains(pegAttempt)) {
                    correctPositions += 1
                }
            }
        }

        return feedback.copy(
            computerCorrectPosition = correctPositions,
            computerCorrectColor = correctColors
        )
    }
}