package com.example.lhmind.domain.usecase

import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.repository.FeedbackValidator
import javax.inject.Inject

class FeedbackValidatorImpl @Inject constructor(
    private val feedbackValidator: FeedbackValidator
) : FeedbackValidator {
    override suspend fun validateAndSave(feedback: Feedback): Feedback {
        return feedbackValidator.validateAndSave(feedback)
    }

    override suspend fun validate(feedback: Feedback): Boolean {
        return feedbackValidator.validate(feedback)
    }
}
