package com.example.lhmind.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lhmind.domain.model.GameStatus
import com.example.lhmind.domain.model.Invitation
import com.example.lhmind.domain.repository.GameRepository
import com.example.lhmind.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _playerId = MutableStateFlow<Long?>(null)
    val playerId: StateFlow<Long?> = _playerId.asStateFlow()

    private val _receivedInvitations = MutableStateFlow<List<Invitation>>(emptyList())
    val receivedInvitations: StateFlow<List<Invitation>> = _receivedInvitations.asStateFlow()

    private val _sentInvitations = MutableStateFlow<List<Invitation>>(emptyList())
    val sentInvitations: StateFlow<List<Invitation>> = _sentInvitations.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        val playerId: Long? = savedStateHandle["playerId"]
        _playerId.value = playerId
        loadInvitations()
    }

    private fun loadInvitations() {
        viewModelScope.launch(Dispatchers.IO) {
            _error.value = null

            try {
                val currentPlayerName = playerRepository.getPlayerById(_playerId.value ?: return@launch).name
                val allGames = gameRepository.getActiveGamesForPlayer(_playerId.value ?: return@launch)
                val invitations = allGames
                    .filter { it.status == GameStatus.INVITATION_SENT }
                    .map { game ->
                        val (playerNameSender, playerNameReceiver) = when (game.creatorIsMaker) {
                            true -> playerRepository.getPlayerById(game.makerId).name to playerRepository.getPlayerById(game.breakerId).name
                            false -> playerRepository.getPlayerById(game.breakerId).name to playerRepository.getPlayerById(game.makerId).name
                        }

                        Invitation(
                            gameId = game.id,
                            playerNameSender = playerNameSender,
                            playerNameReceiver = playerNameReceiver,
                            senderIsMaker = game.creatorIsMaker,
                            startTime = game.startTime
                        )
                    }

                val (sentInvitations, receivedInvitations) = invitations.partition { it.playerNameSender == currentPlayerName }
                _receivedInvitations.value = receivedInvitations
                _sentInvitations.value = sentInvitations
            } catch (e: Exception) {
                _error.value = "Erreur lors du chargement des invitations"
            }
        }
    }

    fun acceptInvitation(gameId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            gameRepository.acceptInvitation(gameId)
            loadInvitations()
        }
    }

    fun cancelInvitation(gameId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            gameRepository.rejectInvitation(gameId)
            loadInvitations()
        }
    }
}
