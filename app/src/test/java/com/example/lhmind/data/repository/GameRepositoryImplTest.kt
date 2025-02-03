package com.example.lhmind.data.repository

import com.example.lhmind.data.local.dao.AttemptDao
import com.example.lhmind.data.local.dao.FeedbackDao
import com.example.lhmind.data.local.dao.GameDao
import com.example.lhmind.data.local.entity.GameEntity
import com.example.lhmind.data.mapper.AttemptMapper
import com.example.lhmind.data.mapper.FeedbackMapper
import com.example.lhmind.data.mapper.GameMapper
import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.model.GameStatus
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.*
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows

class GameRepositoryImplTest {
    private lateinit var gameDao: GameDao
    private lateinit var gameMapper: GameMapper
    private lateinit var attemptDao: AttemptDao
    private lateinit var attemptMapper: AttemptMapper
    private lateinit var feedbackDao: FeedbackDao
    private lateinit var feedbackMapper: FeedbackMapper
    private lateinit var repository: GameRepositoryImpl

    @Before
    fun setup() {
        gameDao = mock()
        gameMapper = mock()
        attemptDao = mock()
        attemptMapper = mock()
        feedbackDao = mock()
        feedbackMapper = mock()
        repository = GameRepositoryImpl(gameDao, gameMapper, attemptDao, attemptMapper, feedbackDao, feedbackMapper)
    }

    @Test
    fun `createGame should create new game with correct initial values`() = runTest {
        // Given
        val makerId = 1L
        val breakerId = 2L
        val expectedGameEntity = GameEntity(
            id = 0,
            makerId = makerId,
            breakerId = breakerId,
            startTime = LocalDateTime.now(),
            endTime = null,
            isWon = false,
            remainingAttempts = 12,
            status = GameStatus.WAITING_FOR_ATTEMPT,
            secretCombination = emptyList()
        )
        val expectedGame = Game(
            id = 1,
            makerId = makerId,
            breakerId = breakerId,
            startTime = expectedGameEntity.startTime,
            endTime = null,
            isWon = false,
            remainingAttempts = 12,
            status = GameStatus.WAITING_FOR_ATTEMPT,
            secretCombination = emptyList()
        )

        whenever(gameDao.insert(any())).thenReturn(1L)
        whenever(gameMapper.mapEntityToDomain(any())).thenReturn(expectedGame)

        // When
        val result = repository.createGame(makerId, breakerId)

        // Then
        verify(gameDao).insert(any())
        assertEquals(expectedGame, result)
    }

    @Test
    fun `getGame should return game when exists`() = runTest {
        // Given
        val gameId = 1L
        val gameEntity = GameEntity(
            id = gameId,
            makerId = 1L,
            breakerId = 2L,
            startTime = LocalDateTime.now(),
            endTime = null,
            isWon = false,
            remainingAttempts = 12,
            status = GameStatus.WAITING_FOR_ATTEMPT,
            secretCombination = emptyList()
        )
        val expectedGame = Game(
            id = gameId,
            makerId = 1L,
            breakerId = 2L,
            startTime = gameEntity.startTime,
            endTime = null,
            isWon = false,
            remainingAttempts = 12,
            status = GameStatus.WAITING_FOR_ATTEMPT,
            secretCombination = emptyList()
        )

        whenever(gameDao.getGameById(gameId)).thenReturn(gameEntity)
        whenever(gameMapper.mapEntityToDomain(gameEntity)).thenReturn(expectedGame)

        // When
        val result = repository.getGame(gameId)

        // Then
        verify(gameDao).getGameById(gameId)
        assertEquals(expectedGame, result)
    }

    @Test
    fun `getGame should throw exception when game not found`() = runTest {
        // Given
        val gameId = 1L
        `when`(gameDao.getGameById(gameId)).thenReturn(null)

        // When/Then
        assertThrows<IllegalArgumentException> {
            repository.getGame(gameId)
        }.apply {
            assertEquals("Game not found", message)
        }
    }

    // Example of testing Flow
    /*@Test
    fun `getActiveGamesForPlayer should return flow of games`() = runTest {
        // Given
        val playerId = 1L
        val gameEntities = listOf(
            GameEntity(
                id = 1L,
                makerId = playerId,
                breakerId = 2L,
                startTime = LocalDateTime.now(),
                endTime = null,
                isWon = false,
                remainingAttempts = 12,
                status = GameStatus.WAITING_FOR_ATTEMPT,
                secretCombination = emptyList()
            )
        )
        val expectedGames = listOf(
            Game(
                id = 1L,
                makerId = playerId,
                breakerId = 2L,
                startTime = gameEntities[0].startTime,
                endTime = null,
                isWon = false,
                remainingAttempts = 12,
                status = GameStatus.WAITING_FOR_ATTEMPT,
                secretCombination = emptyList()
            )
        )

        whenever(gameDao.getActiveGamesForPlayer(playerId))
            .thenReturn(flowOf(gameEntities))
        whenever(gameMapper.mapEntityToDomain(gameEntities[0]))
            .thenReturn(expectedGames[0])

        // When/Then
        repository.getActiveGamesForPlayer(playerId)
            .test {
                assertEquals(expectedGames, awaitItem())
                awaitComplete()
            }
    }*/
}