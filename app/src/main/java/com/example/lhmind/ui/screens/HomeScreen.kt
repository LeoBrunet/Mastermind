package com.example.lhmind.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.lhmind.domain.model.toDisplayString
import com.example.lhmind.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    playerId: Long?,
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val activeGames by viewModel.activeGames.collectAsState()
    val opponentNames by viewModel.opponentNames.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.fetchActiveGames()
            }
        }

        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mastermind") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("createGame/$playerId")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Créer une nouvelle partie"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate("invitations/$playerId")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Voir les invitations")
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
                        val playerIsCreator = when (game.creatorIsMaker) {
                            true -> playerId == game.makerId
                            false -> playerId == game.breakerId
                        }
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
                                    text = "Statut : ${game.status.toDisplayString(isMaker = playerIsMaker, isSender = playerIsCreator)}",
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
