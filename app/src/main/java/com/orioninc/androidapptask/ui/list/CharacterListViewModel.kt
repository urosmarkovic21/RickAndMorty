package com.orioninc.androidapptask.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val repository: CharacterRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CharacterListState(isInitialLoading = true))
    val state: StateFlow<CharacterListState> = _state
    private val displayedCharacters = mutableListOf<Character>()
    private val cachedCharacters = mutableListOf<Character>()

    private var nextPage: Int? = 1
    private var lastPageReached = false
    private var requestInProgress = false

    init {
        loadFirstPage()
    }

    fun retry() {
        viewModelScope.launch {
            requestInProgress = false
            loadFirstPage()
        }
    }

    fun loadFirstPage() {
        if (requestInProgress) return

        viewModelScope.launch {
            requestInProgress = true

            _state.value = CharacterListState(isInitialLoading = true)
            try {
                displayedCharacters.clear()
                cachedCharacters.clear()
                nextPage = 1
                lastPageReached = false

                val response = repository.getCharacters(1)

                displayedCharacters.addAll(response.results)

                nextPage = if (response.info.next != null) 2 else null
                lastPageReached = response.info.next == null
                updateState()
            } catch (e: Exception) {
                _state.value = CharacterListState(
                    isInitialLoading = false,
                    errorMessage = e.message ?: "Failed to load data"
                )
            } finally {
                requestInProgress = false
            }
        }
    }

    private fun updateState() {
        _state.value = CharacterListState(
            characters = displayedCharacters.toList(),
            isInitialLoading = false,
            isLoadingMore = false,
            errorMessage = null,
            endReached = lastPageReached && cachedCharacters.isEmpty()
        )
    }

    private suspend fun consumeBuffer() {
        if (cachedCharacters.size < 10 && !lastPageReached) {
            val page = nextPage ?: return
            val response = repository.getCharacters(page)
            cachedCharacters.addAll(response.results)
            nextPage = if (response.info.next != null) page + 1 else null
            lastPageReached = response.info.next == null
        }
        val chunkSize = minOf(10,cachedCharacters.size)

        if(chunkSize >0){
            val chunk = cachedCharacters.take(chunkSize)
            cachedCharacters.subList(0,chunkSize).clear()
            displayedCharacters.addAll(chunk)
        }
    }

    fun loadNextPage(){
        if(requestInProgress)return
        if(lastPageReached && cachedCharacters.isEmpty())return

        viewModelScope.launch {
            requestInProgress = true
            _state.value=_state.value.copy(
                isLoadingMore = true,
                errorMessage = null
            )

            try {
                consumeBuffer()
                updateState()
            }catch (e: Exception){
                _state.value = _state.value.copy(
                    isLoadingMore = false,
                    errorMessage = e.message?:"Failed to load more"
                )
            }finally {
                requestInProgress = false
            }
        }
    }
}