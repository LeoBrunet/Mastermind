package com.example.lhmind.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lhmind.domain.model.Invitation
import com.example.lhmind.ui.common.TimeHelper
import java.time.LocalDateTime

@Composable
fun InvitationList(
    invitations: List<Invitation>,
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
                invitations.forEach { invitation ->
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
                                    text = if (isSent) "À " + invitation.playerNameReceiver else "De " + invitation.playerNameSender,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = if ((invitation.senderIsMaker && isSent) || (!invitation.senderIsMaker && !isSent)) {
                                        "Vous devrez créer un code"
                                    } else {
                                        "Vous devrez déchiffrer le code"
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = TimeHelper.getRelativeTimeText(invitation.startTime),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            if (isSent) {
                                Button(
                                    onClick = { onCancel(invitation.gameId) },
                                    modifier = Modifier.width(60.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Annuler"
                                    )
                                }
                            } else {
                                Row {
                                    Button(
                                        onClick = { onAccept(invitation.gameId) },
                                        modifier = Modifier.width(60.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Accepter"
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { onReject(invitation.gameId) },
                                        modifier = Modifier.width(60.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Annuler"
                                        )
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

@Preview(showBackground = true)
@Composable
fun PreviewInvitationList() {
    val sampleReceivedInvitations = listOf(
        Invitation(
            gameId = 1L,
            playerNameSender = "Leo",
            playerNameReceiver = "Bob",
            senderIsMaker = true,
            startTime = LocalDateTime.now()
        ),
        Invitation(
            gameId = 2L,
            playerNameSender = "Alice",
            playerNameReceiver = "Bob",
            senderIsMaker = false,
            startTime = LocalDateTime.now()
        )
    )

    val sampleSentInvitations = listOf(
        Invitation(
            gameId = 1L,
            playerNameSender = "Leo",
            playerNameReceiver = "Bob",
            senderIsMaker = true,
            startTime = LocalDateTime.now().minusDays(1)
        ),
        Invitation(
            gameId = 2L,
            playerNameSender = "Alice",
            playerNameReceiver = "Bob",
            senderIsMaker = false,
            startTime = LocalDateTime.now()
        )
    )

    Column {
        Text(text = "Received Invitations", style = MaterialTheme.typography.titleLarge)
        InvitationList(
            invitations = sampleReceivedInvitations,
            isSent = false,
            onAccept = { /* no-op */ },
            onReject = { /* no-op */ },
            onCancel = { /* no-op */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Sent Invitations", style = MaterialTheme.typography.titleLarge)
        InvitationList(
            invitations = sampleSentInvitations,
            isSent = true,
            onAccept = { /* no-op */ },
            onReject = { /* no-op */ },
            onCancel = { /* no-op */ }
        )
    }
}
