package com.example.lhmind.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.lhmind.ui.viewmodel.GameViewModel
import com.example.lhmind.ui.viewmodel.InvitationViewModel

@Composable
fun ReceivedInvitation(
    gameId: Long?,
    playerId: Long?,
    viewModel: GameViewModel = hiltViewModel(),
    invitationViewModel: InvitationViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val game by viewModel.game.collectAsState()
    val players by viewModel.players.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Invitation reçue",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Affichage de l'invitant
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Invitant",
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "Invité par :",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = if (game?.creatorIsMaker == true) players.first().name else players.last().name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Affichage du rôle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Rôle",
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "Votre rôle :",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = if (game?.creatorIsMaker == true) "Breaker" else "Maker",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Explication des rôles
            Text(
                text = if (game?.creatorIsMaker == true) {
                    "En tant que Breaker, vous devrez deviner le code secret créé par votre adversaire."
                } else {
                    "En tant que Maker, vous devrez créer un code secret que votre adversaire devra deviner."
                },
                style = MaterialTheme.typography.bodyMedium
            )

            // Boutons d'action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        // Accepter l'invitation
                        invitationViewModel.acceptInvitation(gameId!!)
                        navController.navigate("game/$gameId/$playerId")
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Accepter",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Text("Accepter")
                    }
                }

                Button(
                    onClick = {
                        // Refuser l'invitation
                        invitationViewModel.cancelInvitation(gameId!!)
                        navController.navigate("home/$playerId") {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Refuser",
                            tint = MaterialTheme.colorScheme.onError
                        )
                        Text("Refuser")
                    }
                }
            }
        }
    }
}