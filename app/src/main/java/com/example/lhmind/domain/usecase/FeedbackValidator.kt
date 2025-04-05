package com.example.lhmind.domain.usecase

import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.repository.FeedbackValidator
import javax.inject.Inject

class FeedbackValidatorImpl @Inject constructor() : FeedbackValidator {
    override suspend fun validateAndSave(feedback: Feedback): Feedback {
        // Validation de la logique du jeu
        return feedback
    }
}
