package com.example.lhmind.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lhmind.ui.viewmodel.GameViewModel

@Composable
fun GameControls(viewModel: GameViewModel) {
    val selectedPegs = viewModel.selectedPegs.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { viewModel.submitAttempt() },
            enabled = selectedPegs.value.size == 4
        ) {
            Text("Valider")
        }
        
        Button(
            onClick = { /* TODO: Reset game */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Rejouer")
        }
    }
}
