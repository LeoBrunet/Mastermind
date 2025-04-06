package com.example.lhmind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lhmind.domain.model.Peg
import com.example.lhmind.domain.model.toColor
import com.example.lhmind.ui.viewmodel.GameViewModel

@Composable
fun CodeMaker(
    viewModel: GameViewModel,
    onCodeCreated: () -> Unit
) {
    var selectedPegs by remember { mutableStateOf<List<Peg>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Créez votre combinaison secrète",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (index < selectedPegs.size) selectedPegs[index].color.toColor()
                            else MaterialTheme.colorScheme.background
                        )
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        PegSelector(
            selectedPegs = selectedPegs,
            onColorSelected = { selectedPegs += Peg(selectedPegs.size, it) },
            onColorRemoved = { selectedPegs.drop(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedPegs.size != 4) {
                    error = "La combinaison doit contenir exactement 4 couleurs"
                    return@Button
                }

                viewModel.createSecretCombination(selectedPegs)
                onCodeCreated()
            },
            enabled = selectedPegs.size == 4
        ) {
            Text("Valider la combinaison")
        }

        // Affichage des erreurs
        error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}