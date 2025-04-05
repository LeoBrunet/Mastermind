package com.example.lhmind.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lhmind.ui.viewmodel.GameViewModel
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ChatComponent(viewModel: GameViewModel) {

    val chatMessages by viewModel.chatMessages.collectAsState()
    val chatMessage by viewModel.chatMessage.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Chat", style = MaterialTheme.typography.titleMedium)
            
            // Messages
            LazyColumn(
                modifier = Modifier
                    .height(100.dp)
                    .padding(vertical = 8.dp)
            ) {
                items(chatMessages) { message ->
                    Text(message)
                }
            }
            
            // Input
            Row {
                OutlinedTextField(
                    value = chatMessage,
                    onValueChange = { viewModel.updateChatMessage(it) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Envoyer un message...") }
                )
                
                Button(
                    onClick = { viewModel.sendChatMessage() },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Envoyer")
                }
            }
        }
    }
}
