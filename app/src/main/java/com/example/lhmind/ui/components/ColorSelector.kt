package com.example.lhmind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lhmind.domain.model.PegColor
import com.example.lhmind.domain.model.toColor
import com.example.lhmind.ui.viewmodel.GameViewModel

@Composable
fun ColorSelector(viewModel: GameViewModel) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PegColor.entries.forEach { pegColor ->
            ColorPeg(
                color = pegColor.toColor(),
                onClick = { viewModel.selectNewPeg(pegColor) }
            )
        }
    }
}

@Composable
private fun ColorPeg(color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color)
            .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .clickable(onClick = onClick)
    )
}
