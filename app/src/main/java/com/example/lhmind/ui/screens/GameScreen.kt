package com.example.lhmind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lhmind.domain.model.GameStatus
import com.example.lhmind.ui.components.GameBoard
import com.example.lhmind.ui.components.PegSelector
import com.example.lhmind.ui.viewmodel.GameViewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameId: Long?,
    viewModel: GameViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val attempts by viewModel.attempts.collectAsState()
    val feedbacks by viewModel.feedbacks.collectAsState()
    val selectedPegs by viewModel.selectedPegs.collectAsState()
    val game by viewModel.game.collectAsState()

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
            when (game?.status) {
                GameStatus.WAITING_FOR_ATTEMPT -> {
                    GameBoard(
                        attempts = attempts,
                        feedbacks = feedbacks,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PegSelector(
                        selectedPegs = selectedPegs,
                        onColorSelected = { viewModel.selectNewPeg(it) },
                        onColorRemoved = { viewModel.removeSelectedPeg(it) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.submitAttempt()
                        },
                        enabled = selectedPegs.size == 4
                    ) {
                        Text("Valider l'essai")
                    }
                }

                GameStatus.INVITATION -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "En attente de réponse de l'invitation...",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }
                }

                GameStatus.WAITING_FOR_FEEDBACK, GameStatus.WRONG_FEEDBACK -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "En attente du retour de l'adversaire...",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }
                }

                GameStatus.COMPLETED -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (game?.makerId == game?.breakerId) {
                                "Gagné ! Vous avez trouvé le code !"
                            } else {
                                "Perdu ! Vous n'avez pas trouvé le code à temps."
                            },
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Le code était : ${game?.secretCombination?.joinToString(" ") { it.color.name }}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                navController.navigate("home") {
                                    popUpTo(0)
                                }
                            }
                        ) {
                            Text("Retour à l'accueil")
                        }
                    }
                }

                GameStatus.ABANDONED -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "La partie a été abandonnée",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                navController.navigate("home") {
                                    popUpTo(0)
                                }
                            }
                        ) {
                            Text("Retour à l'accueil")
                        }
                    }
                }

                null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Erreur : Partie non trouvée",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                navController.navigate("home") {
                                    popUpTo(0)
                                }
                            }
                        ) {
                            Text("Retour à l'accueil")
                        }
                    }
                }
            }
        }
    }
}
