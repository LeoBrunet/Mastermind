package com.example.lhmind.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.repository.GameRepository
import com.example.lhmind.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val _playerId = MutableStateFlow<Long?>(null)
    val playerId: StateFlow<Long?> = _playerId.asStateFlow()

    private val _activeGames = MutableStateFlow<List<Game>>(emptyList())
    val activeGames: StateFlow<List<Game>> = _activeGames.asStateFlow()

    private val _opponentNames = MutableStateFlow<Map<Long, String>>(emptyMap())
    val opponentNames: StateFlow<Map<Long, String>> = _opponentNames.asStateFlow()

    init {
        val playerId: Long? = savedStateHandle["playerId"]
        _playerId.value = playerId
        fetchActiveGames()
    }

    fun fetchActiveGames() {
        viewModelScope.launch(Dispatchers.IO) {
            _playerId.value?.let { playerId ->
                _activeGames.value = gameRepository.getActiveGamesForPlayer(playerId)
                val neededIds = _activeGames.value.map {
                    if (it.makerId == _playerId.value) it.breakerId else it.makerId
                }.distinct()

                val namesMap = neededIds.associateWith { id ->
                    withContext(Dispatchers.IO) {
                        playerRepository.getPlayerById(id).name
                    }
                }

                _opponentNames.value = namesMap
            }
        }
    }
}
