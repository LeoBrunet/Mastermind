package com.example.lhmind.ui.common

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object TimeHelper {

    fun getRelativeTimeText(startTime: LocalDateTime): String {
        val now = LocalDateTime.now()

        val minutes = ChronoUnit.MINUTES.between(startTime, now)
        val hours = ChronoUnit.HOURS.between(startTime, now)
        val days = ChronoUnit.DAYS.between(startTime, now)

        return when {
            days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
            else -> "Just now"
        }
    }
}
