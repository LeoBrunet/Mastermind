package com.example.lhmind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.lhmind.ui.components.InvitationList
import com.example.lhmind.ui.viewmodel.InvitationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationsScreen(
    playerId: Long?,
    navController: NavHostController,
    viewModel: InvitationViewModel = hiltViewModel()
) {

    //todo améliorer l'écran et fix le bug d'affichage des invitations
    val receivedInvitations by viewModel.receivedInvitations.collectAsState()
    val sentInvitations by viewModel.sentInvitations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Invitations") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Section des invitations reçues
            InvitationList(
                invitations = receivedInvitations,
                isSent = false,
                onAccept = { viewModel.acceptInvitation(it) },
                onReject = { viewModel.cancelInvitation(it) },
                onCancel = {viewModel.cancelInvitation(it)},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Section des invitations envoyées
            InvitationList(
                invitations = sentInvitations,
                isSent = true,
                onAccept = { viewModel.acceptInvitation(it) },
                onReject = { viewModel.cancelInvitation(it) },
                onCancel = { viewModel.cancelInvitation(it) },
                modifier = Modifier.fillMaxWidth()
            )

            // Message d'erreur si présent
            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}
