package com.example.lhmind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lhmind.domain.model.GameStatus
import com.example.lhmind.ui.viewmodel.GameViewModel
import androidx.navigation.NavHostController
import com.example.lhmind.domain.model.toDisplayString
import com.example.lhmind.ui.components.AttemptMaker
import com.example.lhmind.ui.components.CodeMaker
import com.example.lhmind.ui.components.FeedbackMaker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameId: Long?,
    playerId: Long?,
    viewModel: GameViewModel = hiltViewModel(),
    navController: NavHostController
) {
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
                GameStatus.WAITING_FOR_CODE_CREATION -> {
                    if (playerId == game?.makerId) {
                        CodeMaker(viewModel) {
                            navController.navigate("game/$gameId/$playerId")
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "En attente de la création du code par l'adversaire...",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            CircularProgressIndicator()
                        }
                    }
                }
                GameStatus.WAITING_FOR_ATTEMPT -> {
                    if (playerId == game?.breakerId) {
                        AttemptMaker(viewModel)
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            game?.status?.toDisplayString(true, isSender = true)?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            CircularProgressIndicator()
                        }
                    }
                }

                GameStatus.WAITING_FOR_FEEDBACK, GameStatus.WRONG_FEEDBACK -> {
                    if (playerId == game?.makerId) {
                        FeedbackMaker(viewModel)
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            game?.status?.toDisplayString(false, isSender = true)?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            CircularProgressIndicator()
                        }
                    }
                }

                GameStatus.INVITATION_SENT -> {
                    // TODO BE ABLE TO REPLY TO INVITATION
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

                /*GameStatus.WAITING_FOR_FEEDBACK, GameStatus.WRONG_FEEDBACK -> {
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
                }*/

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

                GameStatus.ABANDONED, GameStatus.INVITATION_CANCELED -> {
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
