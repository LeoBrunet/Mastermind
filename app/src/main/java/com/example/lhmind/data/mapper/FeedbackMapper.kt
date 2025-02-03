package com.example.lhmind.data.mapper

import com.example.lhmind.data.local.entity.FeedbackEntity
import com.example.lhmind.domain.model.Feedback


class FeedbackMapper {
    fun mapEntityToDomain(entity: FeedbackEntity): Feedback {
        return Feedback(
            id = entity.id,
            attemptId = entity.attemptId,
            correctPosition = entity.correctPosition,
            correctColor = entity.correctColor,
            computerCorrectPosition = entity.computerCorrectPosition,
            computerCorrectColor = entity.computerCorrectColor,
            feedbackTime = entity.feedbackTime,
        )
    }

    fun mapDomainToEntity(domain: Feedback): FeedbackEntity {
        return FeedbackEntity(
            id = domain.id,
            attemptId = domain.attemptId,
            correctPosition = domain.correctPosition,
            correctColor = domain.correctColor,
            computerCorrectPosition = domain.computerCorrectPosition,
            computerCorrectColor = domain.computerCorrectColor,
            feedbackTime = domain.feedbackTime,
        )
    }
}