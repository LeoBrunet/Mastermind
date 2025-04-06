package com.example.lhmind.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lhmind.domain.model.Peg
import com.example.lhmind.ui.viewmodel.GameViewModel

@Composable
fun AttemptMaker(viewModel: GameViewModel) {
    val attempts by viewModel.attempts.collectAsState()
    val feedbacks by viewModel.feedbacks.collectAsState()
    var selectedPegs by remember { mutableStateOf<List<Peg>>(emptyList()) }

    Column {
        GameBoard(
            attempts = attempts,
            feedbacks = feedbacks,
            modifier =  Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        PegSelector(
            selectedPegs = selectedPegs,
            onColorSelected = { selectedPegs += Peg(selectedPegs.size, it) },
            onColorRemoved = { selectedPegs.drop(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.submitAttempt(selectedPegs)
            },
            enabled = selectedPegs.size == 4
        ) {
            Text("Valider l'essai")
        }
    }
}