package com.example.lhmind.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Créer une nouvelle partie",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Entrez le nom de l'adversaire"
        )

        OutlinedTextField(
            value = opponentName,
            onValueChange = { opponentName = it },
            label = { Text("Nom de l'adversaire") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Choisissez votre rôle"
        )

        MakerBreakerSwitch(
            creatorIsMaker = creatorIsMaker,
            onCheckedChange = { creatorIsMaker = it }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp.times(1.5f)),
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

@Composable
fun MakerBreakerSwitch(
    creatorIsMaker: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp.times(1.5f))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val selectedColor = MaterialTheme.colorScheme.primary
                val selectedTextColor = Color.White
                val unselectedTextColor = MaterialTheme.colorScheme.onSurface
                val selectedFontWeight = FontWeight.Bold
                val unselectedFontWeight = FontWeight.Normal

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(if (creatorIsMaker) selectedColor else Color.Transparent)
                        .clickable { onCheckedChange(true) }
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        text = "Maker",
                        color = if (creatorIsMaker) selectedTextColor else unselectedTextColor,
                        fontWeight = if (creatorIsMaker) selectedFontWeight else unselectedFontWeight
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(if (!creatorIsMaker) selectedColor else Color.Transparent)
                        .clickable { onCheckedChange(false) }
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        text = "Breaker",
                        color = if (!creatorIsMaker) selectedTextColor else unselectedTextColor,
                        fontWeight = if (!creatorIsMaker) selectedFontWeight else unselectedFontWeight
                    )
                }
            }
        }
    }
}