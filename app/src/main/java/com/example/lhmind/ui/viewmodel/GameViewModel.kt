package com.example.lhmind.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lhmind.domain.model.Attempt
import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.model.GameStatus
import com.example.lhmind.domain.model.Peg
import com.example.lhmind.domain.usecase.GameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _game = MutableStateFlow<Game?>(null)
    val game: StateFlow<Game?> = _game.asStateFlow()

    private val _attempts = MutableStateFlow<List<Attempt>>(emptyList())
    val attempts: StateFlow<List<Attempt>> = _attempts.asStateFlow()

    private val _feedbacks = MutableStateFlow<List<Feedback>>(emptyList())
    val feedbacks: StateFlow<List<Feedback>> = _feedbacks.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<String>>(emptyList())
    val chatMessages: StateFlow<List<String>> = _chatMessages.asStateFlow()

    private val _chatMessage = MutableStateFlow("")
    val chatMessage: StateFlow<String> = _chatMessage.asStateFlow()

    init {
        val gameId: Long? = savedStateHandle["gameId"]
        gameId?.let {
            fetchGame(it)
            fetchAttempts(it)
        }
    }

    private fun fetchGame(gameId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _game.value = try {
                gameUseCase.getGame(gameId)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun fetchAttempts(gameId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _attempts.value = try { gameUseCase.getAttempts(gameId) } catch (e: Exception) {
                emptyList()
            }
        }
    }

    fun submitAttempt(selectedPegs: List<Peg>) {
        val game = _game.value

        println("submitAttempt called with game status: ${game?.status}")
        if (game == null || game.status != GameStatus.WAITING_FOR_ATTEMPT) return
        if (selectedPegs.size != 4) return

        viewModelScope.launch(Dispatchers.IO) {
            _attempts.value += gameUseCase.makeAttempt(game.id, selectedPegs)
            fetchGame(game.id)
        }
    }

    fun createSecretCombination(pegs: List<Peg>) {
        viewModelScope.launch(Dispatchers.IO) {
            val game = _game.value ?: return@launch
            
            val updatedGame = game.copy(
                status = GameStatus.WAITING_FOR_ATTEMPT,
                secretCombination = pegs
            )
            gameUseCase.createSecretCombination(game.id, pegs)
            _game.value = updatedGame
        }
    }

    fun updateChatMessage(message: String) {
        _chatMessage.value = message
    }

    fun sendChatMessage() {
        if (_chatMessage.value.isNotBlank()) {
            _chatMessages.value += _chatMessage.value
            _chatMessage.value = ""
        }
    }

    suspend fun validateFeedback(feedback: Feedback): Boolean {
        println("validateFeedback called with feedback: $feedback")
        println("validateFeedback returns: ${gameUseCase.validateFeedback(feedback)}")
        return gameUseCase.validateFeedback(feedback)
    }

    fun submitFeedback(feedback: Feedback) {
        val game = _game.value

        println("submitFeedback called with game status: ${game?.status}")
        if (game == null || (game.status != GameStatus.WAITING_FOR_FEEDBACK && game.status != GameStatus.WRONG_FEEDBACK)) return
        viewModelScope.launch(Dispatchers.IO) {
            _feedbacks.value += gameUseCase.provideFeedback(feedback)
            fetchGame(game.id)
        }
    }
}