package com.example.lhmind.data.mapper

import com.example.lhmind.data.local.entity.GameEntity
import com.example.lhmind.domain.model.Game

class GameMapper {
    fun mapEntityToDomain(entity: GameEntity): Game {
        return Game(
            id = entity.id,
            makerId = entity.makerId,
            breakerId = entity.breakerId,
            creatorIsMaker = entity.creatorIsMaker,
            startTime = entity.startTime,
            endTime = entity.endTime,
            remainingAttempts = entity.remainingAttempts,
            status = entity.status,
            secretCombination = entity.secretCombination
        )
    }

    @Suppress("UNUSED")
    fun mapDomainToEntity(domain: Game): GameEntity {
        return GameEntity(
            id = domain.id,
            makerId = domain.makerId,
            breakerId = domain.breakerId,
            creatorIsMaker = domain.creatorIsMaker,
            startTime = domain.startTime,
            endTime = domain.endTime,
            remainingAttempts = domain.remainingAttempts,
            status = domain.status,
            secretCombination = domain.secretCombination
        )
    }
}