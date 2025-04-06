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
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val gameDao: GameDao,
    private val attemptDao: AttemptDao,
    private val feedbackDao: FeedbackDao,
    private val gameMapper: GameMapper,
    private val attemptMapper: AttemptMapper,
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

        println("Attempt ID: $attemptId for Game ID: $gameId with status: ${gameDao.getGameById(gameId)?.status}")

        return attemptMapper.mapEntityToDomain(attemptEntity.copy(id = attemptId))
    }

    override suspend fun getAttempts(gameId: Long): List<Attempt> {
        return attemptDao.getAttemptsForGame(gameId).map { attemptMapper.mapEntityToDomain(it) }
    }

    override suspend fun saveFeedback(feedback: Feedback): Feedback {
        val feedbackEntity = feedbackMapper.mapDomainToEntity(feedback)
        val feedbackId = feedbackDao.insert(feedbackEntity)
        return feedbackMapper.mapEntityToDomain(feedbackEntity.copy(id = feedbackId))
    }

    override suspend fun getActiveGamesForPlayer(playerId: Long): List<Game> {
        return gameDao.getActiveGamesForPlayer(playerId).map { gameMapper.mapEntityToDomain(it) }
    }

    override suspend fun createSecretCombination(gameId: Long, pegs: List<Peg>) {
        if (pegs.size < 4) { throw IllegalArgumentException("Secret combination must have at least 4 pegs") }
        val game = gameDao.getGameById(gameId) ?: throw IllegalArgumentException("Game not found")
        if (game.status != GameStatus.WAITING_FOR_ATTEMPT) { throw IllegalArgumentException("Game already have secret combination")}
        gameDao.update(game.copy(status = GameStatus.WAITING_FOR_ATTEMPT))
        return gameDao.createSecretCombination(gameId, pegs)
    }
}