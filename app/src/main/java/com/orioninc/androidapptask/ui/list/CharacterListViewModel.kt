package com.orioninc.androidapptask.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orioninc.androidapptask.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel (
    private val repository: CharacterRepository
) : ViewModel(){
    private val _state = MutableStateFlow<CharacterListState>(CharacterListState.Loading)
    val state: StateFlow<CharacterListState> = _state

    init {
        fetchCharacters()
    }

    fun retry() {
        fetchCharacters()
    }

    private fun fetchCharacters() {
        viewModelScope.launch {
            _state.value = CharacterListState.Loading
            try {
                val response = repository.getCharacters()
                _state.value = CharacterListState.Success(response.results)
            } catch (e: Exception) {
                _state.value = CharacterListState.Error(e.message ?: "Unknown error")
            }
        }
    }
}