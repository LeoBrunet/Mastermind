package com.example.lhmind.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lhmind.domain.model.Attempt
import com.example.lhmind.domain.model.Feedback
import com.example.lhmind.domain.model.Game
import com.example.lhmind.domain.model.GameStatus
import com.example.lhmind.domain.model.Peg
import com.example.lhmind.domain.model.PegColor
import com.example.lhmind.domain.usecase.GameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val gameId: Long? = savedStateHandle["gameId"]

    private val _game = MutableStateFlow<Game?>(null)
    val game: StateFlow<Game?> = _game.asStateFlow()

    init {
        val gameId: Long? = savedStateHandle["gameId"]
        gameId?.let { fetchGame(it) }
    }

    private fun fetchGame(gameId: Long) {
        viewModelScope.launch {
            _game.value = try {
                gameUseCase.getGame(gameId)
            } catch (e: Exception) {
                null
            }
        }
    }
    
    private val _attempts = MutableStateFlow<List<Attempt>>(emptyList())
    val attempts: StateFlow<List<Attempt>> = _attempts.asStateFlow()
    
    private val _feedbacks = MutableStateFlow<List<Feedback>>(emptyList())
    val feedbacks: StateFlow<List<Feedback>> = _feedbacks.asStateFlow()
    
    private val _currentRow = MutableStateFlow(0)
    val currentRow: StateFlow<Int> = _currentRow.asStateFlow()
    
    private val _selectedPegs = MutableStateFlow<List<Peg>>(emptyList())
    val selectedPegs: StateFlow<List<Peg>> = _selectedPegs.asStateFlow()
    
    private val _chatMessages = MutableStateFlow<List<String>>(emptyList())
    val chatMessages: StateFlow<List<String>> = _chatMessages.asStateFlow()

    private val _chatMessage = MutableStateFlow("")
    val chatMessage: StateFlow<String> = _chatMessage.asStateFlow()

    // Méthodes pour le GameBoard
    fun getPegColor(row: Int, col: Int): PegColor {
        return attempts.value.getOrNull(row)?.pegs?.getOrNull(col)?.color ?: PegColor.NONE
    }
    
    fun getCorrectPositions(row: Int): Int {
        return feedbacks.value[row].correctColor
    }
    
    fun getCorrectColors(row: Int): Int {
        return feedbacks.value[row].correctPosition
    }
    
    fun selectNewPeg(color: PegColor) {
        if (_selectedPegs.value.size < 4) {
            _selectedPegs.value += Peg(position = _selectedPegs.value.size + 1, color)
        }
    }

    fun removeSelectedPeg(position: Int) {
        _selectedPegs.value = _selectedPegs.value.filter { it.position != position }
    }

    fun submitAttempt() {
        val game = _game.value
        val selected = _selectedPegs.value

        println("submitAttempt called with game status: ${game?.status}")
        if (game == null || game.status != GameStatus.WAITING_FOR_ATTEMPT) return
        if (selected.size != 4) return

        viewModelScope.launch {
            val attempt = gameUseCase.makeAttempt(game.id, selected)

            _attempts.value += attempt
            _currentRow.value++
            _selectedPegs.value = emptyList()
            fetchGame(game.id)
        }
    }


    private fun calculateFeedback(attempt: List<Color>): Pair<Int, Int> {
        // TODO: Implémenter la logique réelle de validation
        // Pour l'instant, simulation aléatoire
        val correctPositions = attempt.take(2).count() // Exemple simplifié
        val correctColors = attempt.takeLast(2).count() // Exemple simplifié
        return correctPositions to correctColors
    }

    fun updateChatMessage(message: String) {
        _chatMessage.value = message
    }

    fun sendChatMessage() {
        if (_chatMessage.value.isNotBlank()) {
            _chatMessages.value = _chatMessages.value + _chatMessage.value
            _chatMessage.value = ""
        }
    }
}