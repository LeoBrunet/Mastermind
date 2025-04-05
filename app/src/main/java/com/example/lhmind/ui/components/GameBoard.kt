package com.example.lhmind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lhmind.domain.model.Attempt
import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.model.PegColor
import com.example.lhmind.domain.model.toColor
import com.example.lhmind.ui.viewmodel.GameViewModel

@Composable
fun GameBoard(
    attempts: List<Attempt>,
    feedbacks: List<Feedback>,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 10 lignes pour les tentatives
        repeat(5) { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Tentative
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val attempt = attempts.getOrNull(row)
                    repeat(4) { pegIndex ->
                        attempt?.pegs?.getOrNull(pegIndex)?.color?.toColor()?.let {
                            Surface(
                                color = it,
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                            ) {
                                // Empty
                            }
                        }
                    }
                }

                // Feedback
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val feedback = feedbacks.getOrNull(row)
                    repeat(4) {
                        Surface(
                            color = when {
                                feedback?.correctPosition == 4 && it < feedback.correctPosition -> Color.Black
                                feedback?.correctColor == 4 && it < feedback.correctColor -> Color.White
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = CircleShape,
                            modifier = Modifier
                                .size(16.dp)
                        ) {
                            // Empty
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeedbackIndicators(correctPosition: Int, correctColor: Int) {
    Row(
        modifier = Modifier.width(100.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(correctPosition) {
            Surface(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
            ) {
                // Empty
            }
        }
        repeat(correctColor) {
            Surface(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Color.Gray, CircleShape)
            ) {
                // Empty
            }
        }
    }
}