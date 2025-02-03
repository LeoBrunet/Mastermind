package com.example.lhmind.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lhmind.domain.usecase.GameUseCase
import com.example.lhmind.ui.state.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameUseCase: GameUseCase
) : ViewModel() {
    private val _gameState = MutableStateFlow<GameState>(GameState.Initial)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun createGame(makerId: Long, breakerId: Long) {
        viewModelScope.launch {
            try {
                val game = gameUseCase.createGame(makerId, breakerId)
                _gameState.value = GameState.GameCreated(game)
            } catch (e: Exception) {
                _gameState.value = GameState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // Add other ViewModel methods...
}