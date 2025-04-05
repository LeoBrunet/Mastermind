package com.example.lhmind.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lhmind.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {
    
    fun registerPlayer(name: String, onPlayerRegistered: (Long) -> Unit) {
        viewModelScope.launch {
            try {
                val playerId: Long = playerRepository.registerPlayer(name)
                onPlayerRegistered(playerId)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
