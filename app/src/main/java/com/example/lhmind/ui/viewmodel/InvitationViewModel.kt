package com.example.lhmind.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.model.GameStatus
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

    private val _receivedInvitations = MutableStateFlow<List<Game>>(emptyList())
    val receivedInvitations: StateFlow<List<Game>> = _receivedInvitations.asStateFlow()

    private val _sentInvitations = MutableStateFlow<List<Game>>(emptyList())
    val sentInvitations: StateFlow<List<Game>> = _sentInvitations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        val playerId: Long? = savedStateHandle["playerId"]
        _playerId.value = playerId
        loadInvitations()
    }

    private fun loadInvitations() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null

            println("player id : ${_playerId.value}")
            println("player id : ${gameRepository.getActiveGamesForPlayer(_playerId.value!!)}")

            try {
                val allInvitations = gameRepository.getActiveGamesForPlayer(_playerId.value ?: return@launch)
                val (received, sent) = allInvitations.partition { it.status == GameStatus.INVITATION_SENT }
                
                _receivedInvitations.value = received
                _sentInvitations.value = sent
            } catch (e: Exception) {
                _error.value = "Erreur lors du chargement des invitations"
            }
            
            _isLoading.value = false
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
