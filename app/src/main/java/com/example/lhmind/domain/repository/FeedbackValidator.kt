package com.example.lhmind.domain.repository

import com.example.lhmind.domain.model.Feedback

interface FeedbackValidator {
    suspend fun validateAndSave(feedback: Feedback): Feedback
}