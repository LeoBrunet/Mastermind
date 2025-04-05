package com.example.lhmind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.lhmind.ui.viewmodel.CreateGameViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateGameScreen(
    playerId: Long?,
    viewModel: CreateGameViewModel = hiltViewModel(),
    onGameCreated: (Long) -> Unit
) {
    var opponentName by remember { mutableStateOf("") }
    var creatorIsMaker by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(playerId) {
        if (playerId != null) {
            viewModel.fetchPlayer(playerId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Créer une nouvelle partie",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = opponentName,
            onValueChange = { opponentName = it },
            label = { Text("Nom de l'adversaire") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Switch(
            checked = creatorIsMaker,
            onCheckedChange = { creatorIsMaker = it },
            modifier = Modifier.align(Alignment.Start)
        )

        Button(
            onClick = {
                if (opponentName.isBlank()) {
                    error = "Veuillez entrer un nom pour l'adversaire"
                    return@Button
                }

                coroutineScope.launch {
                    try {
                        onGameCreated(viewModel.createGame(opponentName, creatorIsMaker = creatorIsMaker))
                    } catch (e: IllegalArgumentException) {
                        error = e.message
                    }
                }
            },
            enabled = opponentName.isNotBlank()
        ) {
            Text("Créer la partie")
        }

        if (error != null) {
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
