package com.example.lhmind.data.mapper

import com.example.lhmind.data.local.entity.GameEntity
import com.example.lhmind.domain.model.Game

class GameMapper {
    fun mapEntityToDomain(entity: GameEntity): Game {
        return Game(
            id = entity.id,
            makerId = entity.makerId,
            breakerId = entity.breakerId,
            startTime = entity.startTime,
            endTime = entity.endTime,
            isWon = entity.isWon,
            remainingAttempts = entity.remainingAttempts,
            status = entity.status,
            secretCombination = entity.secretCombination
        )
    }

    fun mapDomainToEntity(domain: Game): GameEntity {
        return GameEntity(
            id = domain.id,
            makerId = domain.makerId,
            breakerId = domain.breakerId,
            startTime = domain.startTime,
            endTime = domain.endTime,
            isWon = domain.isWon,
            remainingAttempts = domain.remainingAttempts,
            status = domain.status,
            secretCombination = domain.secretCombination
        )
    }
}