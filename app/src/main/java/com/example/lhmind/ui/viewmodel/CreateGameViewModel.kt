package com.example.lhmind.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lhmind.domain.model.Player
import com.example.lhmind.domain.usecase.GameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGameViewModel @Inject constructor(
    private val gameUseCase: GameUseCase
) : ViewModel() {

    private val _player = MutableStateFlow<Player?>(null)

    suspend fun createGame(
        playerInvitedName: String,
        creatorIsMaker: Boolean
    ): Long {
        val creatorPlayer = _player.value ?: throw IllegalArgumentException("Player not found.")

        val invitedPlayer = try {
            gameUseCase.getPlayer(playerInvitedName)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invited player not found.")
        }

        if (creatorPlayer.id == invitedPlayer.id) {
            throw IllegalArgumentException("Creator and invited players cannot be the same.")
        }

        return gameUseCase.createGame(
            makerId = if (creatorIsMaker) creatorPlayer.id else invitedPlayer.id,
            breakerId = if (creatorIsMaker) invitedPlayer.id else creatorPlayer.id,
            creatorIsMaker = creatorIsMaker
        ).id
    }

    fun fetchPlayer(playerId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _player.value = gameUseCase.getPlayerById(playerId)
        }
    }
}
