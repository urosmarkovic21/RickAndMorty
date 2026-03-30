package com.orioninc.androidapptask.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orioninc.androidapptask.domain.GetCharacterUseCase
import com.orioninc.androidapptask.domain.RefreshCharacterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val getCharacterUseCase: GetCharacterUseCase,
    private val refreshCharacterUseCase: RefreshCharacterUseCase,
    private val characterId: Int,
) : ViewModel() {
    private val _state = MutableStateFlow<CharacterDetailState>(CharacterDetailState.Loading)
    val state: StateFlow<CharacterDetailState> = _state

    init {
        observeCharacter()
        refreshCharacter()
    }

    private fun observeCharacter() {
        viewModelScope.launch {
            getCharacterUseCase(characterId).collect { character ->
                _state.value =
                    if (character != null) {
                        CharacterDetailState.Success(character)
                    } else {
                        CharacterDetailState.Loading
                    }
            }
        }
    }

    private fun refreshCharacter() {
        viewModelScope.launch {
            try {
                refreshCharacterUseCase(characterId)
            } catch (e: Exception) {
                _state.value =
                    CharacterDetailState.Error(
                        e.message ?: "Unknown error",
                    )
            }
        }
    }
}
