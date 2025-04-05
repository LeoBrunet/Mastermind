package com.example.lhmind.domain.model

import androidx.compose.ui.graphics.Color

enum class PegColor {
    RED, BLUE, GREEN, YELLOW, WHITE, BLACK, ORANGE, PURPLE, NONE
}

fun PegColor.toColor(): Color =
    when (this) {
        PegColor.RED -> Color.Red
        PegColor.GREEN -> Color.Green
        PegColor.BLUE -> Color.Blue
        PegColor.YELLOW -> Color.Yellow
        PegColor.ORANGE -> Color(0xFFFFA500)
        PegColor.PURPLE -> Color(0xFF800080)
        PegColor.WHITE -> Color.White
        PegColor.BLACK -> Color.Black
        PegColor.NONE -> Color.Transparent
    }