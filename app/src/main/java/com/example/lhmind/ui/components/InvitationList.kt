package com.example.lhmind.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.model.GameStatus
import com.example.lhmind.domain.model.toDisplayString

@Composable
fun InvitationList(
    invitations: List<Game>,
    isSent: Boolean,
    onAccept: (Long) -> Unit,
    onReject: (Long) -> Unit,
    onCancel: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = if (isSent) "Invitations envoyées" else "Invitations reçues",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (invitations.isEmpty()) {
            Text(
                text = if (isSent) "Aucune invitation envoyée" else "Aucune invitation reçue",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            Column {
                invitations.forEach { game ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = if (isSent) "${game.breakerId} → ${game.makerId}" else "${game.makerId} → ${game.breakerId}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = game.startTime.toString(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            if (isSent) {
                                Button(
                                    onClick = { onCancel(game.id) },
                                    modifier = Modifier.width(120.dp)
                                ) {
                                    Text("Annuler")
                                }
                            } else {
                                Row {
                                    Button(
                                        onClick = { onAccept(game.id) },
                                        modifier = Modifier.width(120.dp)
                                    ) {
                                        Text("Accepter")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { onReject(game.id) },
                                        modifier = Modifier.width(120.dp)
                                    ) {
                                        Text("Refuser")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
