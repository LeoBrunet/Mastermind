package com.example.lhmind.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.lhmind.ui.components.InvitationList
import com.example.lhmind.ui.viewmodel.InvitationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationsScreen(
    navController: NavHostController,
    viewModel: InvitationViewModel = hiltViewModel()
) {

    val receivedInvitations by viewModel.receivedInvitations.collectAsState()
    val sentInvitations by viewModel.sentInvitations.collectAsState()
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