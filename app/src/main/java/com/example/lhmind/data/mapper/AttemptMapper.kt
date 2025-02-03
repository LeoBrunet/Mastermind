package com.example.lhmind.data.mapper

import com.example.lhmind.data.local.entity.AttemptEntity
import com.example.lhmind.domain.model.Attempt

class AttemptMapper {
    fun mapEntityToDomain(entity: AttemptEntity): Attempt {
        return Attempt(
            id = entity.id,
            gameId = entity.gameId,
            attemptNumber = entity.attemptNumber,
            attemptTime =  entity.attemptTime,
            pegs = entity.pegs
        )
    }

    fun mapDomainToEntity(domain: Attempt): AttemptEntity {
        return AttemptEntity(
            id = domain.id,
            gameId = domain.gameId,
            attemptNumber = domain.attemptNumber,
            attemptTime =  domain.attemptTime,
            pegs = domain.pegs
        )
    }
}