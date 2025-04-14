package com.example.lhmind.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.lhmind.domain.model.GameStatus
import com.example.lhmind.domain.model.toDisplayString
import com.example.lhmind.ui.components.AttemptMaker
import com.example.lhmind.ui.components.CodeMaker
import com.example.lhmind.ui.components.FeedbackMaker
import com.example.lhmind.ui.components.ReceivedInvitation
import com.example.lhmind.ui.components.WaitingScreen
import com.example.lhmind.ui.viewmodel.GameViewModel
import com.example.lhmind.ui.viewmodel.InvitationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameId: Long?,
    playerId: Long?,
    viewModel: GameViewModel = hiltViewModel(),
    invitationViewModel: InvitationViewModel = hiltViewModel(),
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
                        WaitingScreen(
                            message = "En attente de la création du code par l'adversaire...",
                            destination = "home/$playerId",
                            destinationText = "Retour au menu",
                            navController = navController
                        )
                    }
                }

                GameStatus.WAITING_FOR_ATTEMPT -> {
                    if (playerId == game?.breakerId) {
                        AttemptMaker(viewModel)
                    } else {
                        game?.status?.toDisplayString(true, isSender = true)?.let {
                            WaitingScreen(
                                message = it,
                                destination = "home/$playerId",
                                destinationText = "Retour au menu",
                                navController = navController
                            )
                        }
                    }
                }

                GameStatus.WAITING_FOR_FEEDBACK, GameStatus.WRONG_FEEDBACK -> {
                    if (playerId == game?.makerId) {
                        FeedbackMaker(viewModel)
                    } else {
                        game?.status?.toDisplayString(false, isSender = true)?.let {
                            WaitingScreen(
                                message = it,
                                destination = "home/$playerId",
                                destinationText = "Retour au menu",
                                navController = navController
                            )
                        }
                    }
                }

                GameStatus.INVITATION_SENT -> {
                    if ((game?.creatorIsMaker == true && game?.makerId == playerId) or (game?.creatorIsMaker == false && game?.breakerId == playerId)) {
                        WaitingScreen(
                            message = "En attente de réponse de l'invitation...",
                            destination = "home/$playerId",
                            destinationText = "Retour au menu",
                            navController = navController
                        )
                    } else {
                        ReceivedInvitation(
                            gameId = gameId,
                            playerId = playerId,
                            viewModel = viewModel,
                            invitationViewModel = invitationViewModel,
                            navController = navController
                        )
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
                                navController.navigate("home/$playerId") {
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
                                navController.navigate("home/$playerId") {
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
                                navController.navigate("home/$playerId") {
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
