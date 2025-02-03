package com.example.lhmind.data.repository

import com.example.lhmind.data.local.dao.AttemptDao
import com.example.lhmind.data.local.dao.FeedbackDao
import com.example.lhmind.data.local.dao.GameDao
import com.example.lhmind.data.local.entity.AttemptEntity
import com.example.lhmind.data.local.entity.GameEntity
import com.example.lhmind.data.mapper.AttemptMapper
import com.example.lhmind.data.mapper.FeedbackMapper
import com.example.lhmind.data.mapper.GameMapper
import com.example.lhmind.domain.model.Attempt
import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.model.GameStatus
import com.example.lhmind.domain.model.Peg
import com.example.lhmind.domain.repository.GameRepository
import java.time.LocalDateTime

class GameRepositoryImpl(
    private val gameDao: GameDao,
    private val gameMapper: GameMapper,
    private val attemptDao: AttemptDao,
    private val attemptMapper: AttemptMapper,
    private val feedbackDao: FeedbackDao,
    private val feedbackMapper: FeedbackMapper
) : GameRepository {
    override suspend fun createGame(makerId: Long, breakerId: Long): Game {
        val gameEntity = GameEntity(
            makerId = makerId,
            breakerId = breakerId,
            startTime = LocalDateTime.now(),
            endTime = null,
            isWon = false,
            remainingAttempts = 12,
            status = GameStatus.WAITING_FOR_ATTEMPT,
            secretCombination = emptyList()
        )

        val gameId = gameDao.insert(gameEntity)
        return gameMapper.mapEntityToDomain(gameEntity.copy(id = gameId))
    }

    override suspend fun getGame(gameId: Long): Game {
        val gameEntity = gameDao.getGameById(gameId)
            ?: throw IllegalArgumentException("Game not found")
        return gameMapper.mapEntityToDomain(gameEntity)
    }

    override suspend fun makeAttempt(gameId: Long, pegs: List<Peg>): Attempt {
        val attemptNumber = attemptDao.getNumberOfAttemptForGame(gameId)

        val attemptEntity = AttemptEntity(
            gameId = gameId,
            attemptTime = LocalDateTime.now(),
            attemptNumber = attemptNumber + 1,
            pegs = pegs
        )

        val attemptId = attemptDao.insert(attemptEntity)

        gameDao.decrementRemainingAttempts(gameId)
        gameDao.getGameById(gameId)?.copy(status = GameStatus.WAITING_FOR_FEEDBACK)
            ?.let { gameDao.update(it) }

        return attemptMapper.mapEntityToDomain(attemptEntity.copy(id = attemptId))
    }

    override suspend fun saveFeedback(feedback: Feedback): Feedback {
        //val feedbackEntityExisting = feedbackDao.getFeedbackByAttemptId(feedback.attemptId)
        val feedbackEntity = feedbackMapper.mapDomainToEntity(feedback)

        /*val feedbackId = if (feedbackEntityExisting != null) {
            feedbackDao.update(feedbackEntity.copy(id = feedbackEntityExisting.id))
            feedbackEntityExisting.id
        } else {
            feedbackDao.insert(feedbackEntity)
        }*/
        val feedbackId = feedbackDao.insert(feedbackEntity)

        return feedbackMapper.mapEntityToDomain(feedbackEntity.copy(id = feedbackId))
    }
}