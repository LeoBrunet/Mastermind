package com.example.lhmind.di

import android.content.Context
import com.example.lhmind.data.local.MastermindDatabase
import com.example.lhmind.data.local.dao.GameDao
import com.example.lhmind.data.local.dao.AttemptDao
import com.example.lhmind.data.local.dao.FeedbackDao
import com.example.lhmind.data.local.dao.PlayerDao
import com.example.lhmind.data.repository.GameRepositoryImpl
import com.example.lhmind.data.mapper.GameMapper
import com.example.lhmind.data.mapper.AttemptMapper
import com.example.lhmind.data.mapper.FeedbackMapper
import com.example.lhmind.data.mapper.PlayerMapper
import com.example.lhmind.domain.repository.GameRepository
import com.example.lhmind.domain.repository.FeedbackValidator
import com.example.lhmind.data.repository.FeedbackValidatorImpl
import com.example.lhmind.data.repository.PlayerRepositoryImpl
import com.example.lhmind.domain.repository.PlayerRepository
import com.example.lhmind.domain.usecase.GameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MastermindDatabase {
        return MastermindDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideGameDao(database: MastermindDatabase): GameDao {
        return database.gameDao()
    }

    @Provides
    @Singleton
    fun providePlayerDao(database: MastermindDatabase): PlayerDao {
        return database.playerDao()
    }

    @Provides
    @Singleton
    fun provideAttemptDao(database: MastermindDatabase): AttemptDao {
        return database.attemptDao()
    }

    @Provides
    @Singleton
    fun provideFeedbackDao(database: MastermindDatabase): FeedbackDao {
        return database.feedbackDao()
    }

    @Provides
    @Singleton
    fun provideGameMapper(): GameMapper {
        return GameMapper()
    }

    @Provides
    @Singleton
    fun provideAttemptMapper(): AttemptMapper {
        return AttemptMapper()
    }

    @Provides
    @Singleton
    fun provideFeedbackMapper(): FeedbackMapper {
        return FeedbackMapper()
    }

    @Provides
    @Singleton
    fun providePlayerMapper(): PlayerMapper {
        return PlayerMapper()
    }

    @Provides
    @Singleton
    fun provideGameRepository(
        gameDao: GameDao,
        gameMapper: GameMapper,
        attemptDao: AttemptDao,
        attemptMapper: AttemptMapper,
        feedbackDao: FeedbackDao,
        feedbackMapper: FeedbackMapper
    ): GameRepository {
        return GameRepositoryImpl(
            gameDao = gameDao,
            gameMapper = gameMapper,
            attemptDao = attemptDao,
            attemptMapper = attemptMapper,
            feedbackDao = feedbackDao,
            feedbackMapper = feedbackMapper
        )
    }

    @Provides
    @Singleton
    fun provideFeedbackValidator(
        gameRepository: GameRepository,
        attemptDao: AttemptDao,
        gameDao: GameDao,
        gameMapper: GameMapper
    ): FeedbackValidator {
        return FeedbackValidatorImpl(
            gameRepository = gameRepository,
            attemptDao = attemptDao,
            gameDao = gameDao,
            gameMapper = gameMapper
        )
    }

    @Provides
    @Singleton
    fun providePlayerRepository(
        playerDao: PlayerDao,
        playerMapper: PlayerMapper,
        gameMapper: GameMapper
    ): PlayerRepository {
        return PlayerRepositoryImpl(
            playerDao = playerDao,
            playerMapper = playerMapper,
            gameMapper = gameMapper
        )
    }

    @Provides
    @Singleton
    fun provideGameUseCase(
        gameRepository: GameRepository,
        playerRepository: PlayerRepository,
        validator: FeedbackValidator
    ): GameUseCase {
        return GameUseCase(
            gameRepository = gameRepository,
            playerRepository = playerRepository,
            feedbackValidator = validator
        )
    }
}
