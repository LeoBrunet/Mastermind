package com.example.lhmind.data.mapper

import com.example.lhmind.data.local.entity.PlayerEntity
import com.example.lhmind.domain.model.Player


class PlayerMapper {
    fun mapEntityToDomain(entity: PlayerEntity): Player {
        return Player(
            id = entity.id,
            username = entity.username,
            registrationDate = entity.registrationDate
        )
    }

    fun mapDomainToEntity(domain: Player): PlayerEntity {
        return PlayerEntity(
            id = domain.id,
            username = domain.username,
            registrationDate = domain.registrationDate
        )
    }
}