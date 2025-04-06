package com.example.lhmind.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.lhmind.domain.model.toDisplayString
import com.example.lhmind.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    playerId: Long?,
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val activeGames by viewModel.activeGames.collectAsState()
    val opponentNames by viewModel.opponentNames.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mastermind") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    navController.navigate("createGame/$playerId")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Créer une nouvelle partie")
            }

            if (activeGames.isNotEmpty()) {
                Text(
                    text = "Parties en cours",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(activeGames) { game ->
                        val playerIsMaker = game.makerId == playerId
                        val opponentId = if (playerIsMaker) game.breakerId else game.makerId
                        val opponentName = opponentNames[opponentId] ?: "Chargement..."

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    navController.navigate("game/${game.id}/${playerId}")
                                },
                            onClick = {
                                navController.navigate("game/${game.id}/${playerId}")
                            }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = "${if (playerIsMaker) "[MAKER]" else "[BREAKER]"} Partie avec $opponentName",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Statut : ${game.status.toDisplayString(isMaker = playerIsMaker)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "Aucune partie en cours",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}
