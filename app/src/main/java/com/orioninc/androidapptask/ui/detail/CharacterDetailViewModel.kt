package com.orioninc.androidapptask.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orioninc.androidapptask.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val repository: CharacterRepository,
    private val characterId: Int
) : ViewModel() {

    private val _state = MutableStateFlow<CharacterDetailState>(CharacterDetailState.Loading)
    val state: StateFlow<CharacterDetailState> = _state

    init {
        fetchCharacter()
    }

    private fun fetchCharacter() {
        viewModelScope.launch {
            _state.value = CharacterDetailState.Loading
            try {
                val character = repository.getCharacter(characterId)
                _state.value = CharacterDetailState.Success(character)
            } catch (e: Exception) {
                _state.value = CharacterDetailState.Error(e.message ?: "Unknown error")
            }
        }
    }
}