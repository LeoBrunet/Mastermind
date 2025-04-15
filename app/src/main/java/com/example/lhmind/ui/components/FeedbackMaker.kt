package com.example.lhmind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.model.toColor
import com.example.lhmind.ui.viewmodel.GameViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

//
@Composable
fun FeedbackMaker(
    viewModel: GameViewModel
) {
    val game by viewModel.game.collectAsState()
    val attempts by viewModel.attempts.collectAsState()
    var error by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Votre combinaison secrète",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        game?.secretCombination?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ){
                it.forEach { peg ->
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(peg.color.toColor())
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Text(
            text = "Donnez un feedback sur la tentative de votre adversaire",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (attempts.isNotEmpty()) {
            attempts.last().let { attempt ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    attempt.pegs.forEach { peg ->
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(peg.color.toColor())
                                .padding(4.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        } else {
            Text(
                text = "Aucune tentative n'a été faite",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sélecteur de feedback
        var correctPosition by remember { mutableIntStateOf(0) }
        var correctColor by remember { mutableIntStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Column {
                Text("Correct position")
                Slider(
                    value = correctPosition.toFloat(),
                    onValueChange = { correctPosition = it.toInt() },
                    valueRange = 0f..4f,
                    steps = 3
                )
            }
            Column {
                Text("Correct color")
                Slider(
                    value = correctColor.toFloat(),
                    onValueChange = { correctColor = it.toInt() },
                    valueRange = 0f..4f,
                    steps = 3
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val feedback = Feedback(
                    attemptId = attempts.last().id,
                    correctPosition = correctPosition,
                    correctColor = correctColor,
                    feedbackTime = LocalDateTime.now()
                )
                coroutineScope.launch {
                    if (viewModel.validateFeedback(feedback)) {
                        viewModel.submitFeedback(feedback)
                    } else {
                        error = "Mauvais feedback !"
                    }
                }
            },
            enabled = correctPosition <= 4 && correctColor <= 4
        ) {
            Text("Valider le feedback")
        }
    }
}