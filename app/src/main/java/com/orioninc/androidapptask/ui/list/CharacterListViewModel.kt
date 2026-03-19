package com.orioninc.androidapptask.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.data.repository.CharacterRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val repository: CharacterRepository
) : ViewModel() {
    private val _state = MutableStateFlow<CharacterListState>(CharacterListState.Loading)
    val state: StateFlow<CharacterListState> = _state

    private var currentPage = 1
    private var hasNextPage = true
    private val allCharacters = mutableListOf<Character>()
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private var isRequestInFlight = false
    private var lastRequestTime = 0L

    init {
        loadNextPage()
    }

    fun retry() {
        loadNextPage()
    }

    fun loadNextPage() {
        val now = System.currentTimeMillis()
        if (isRequestInFlight || !hasNextPage || now - lastRequestTime < 500) return

        isRequestInFlight = true
        _isLoadingMore.value = true
        lastRequestTime = now

        viewModelScope.launch {
            try {
                val response = repository.getCharacters(currentPage)
                allCharacters.addAll(response.results)
                hasNextPage = response.info.next != null
                currentPage++
                _state.value = CharacterListState.Success(allCharacters.toList())
            } catch (e: Exception) {
                _state.value = CharacterListState.Error(e.message ?: "Unknown error")
            } finally {
                _isLoadingMore.value = false
                isRequestInFlight = false
            }
        }
    }
}