package com.orioninc.androidapptask.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.domain.GetCharactersUseCase
import com.orioninc.androidapptask.domain.GetSavedCharactersRefreshResultUseCase
import com.orioninc.androidapptask.domain.RefreshCharactersUseCase
import com.orioninc.androidapptask.util.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val refreshCharactersUseCase: RefreshCharactersUseCase,
    private val getSavedCharactersRefreshResultUseCase: GetSavedCharactersRefreshResultUseCase,
    private val notificationHelper: NotificationHelper,
) : ViewModel() {
    private val _state = MutableStateFlow(CharacterListState(isInitialLoading = true))
    val state: StateFlow<CharacterListState> = _state
    private var allLoadedCharacters: List<Character> = emptyList()
    private var visibleCount = 10
    private var nextPage: Int? = 1
    private var requestInProgress = false

    init {
        observeCharacters()
        refreshInitial()
    }

    fun retry() {
        if (_state.value.characters.isEmpty()) {
            refreshInitial()
        } else {
            loadNextPage()
        }
    }

    fun refreshFromStart() {
        refreshInitial()
    }

    private fun observeCharacters() {
        viewModelScope.launch {
            getCharactersUseCase().collect { characters ->
                allLoadedCharacters = characters
                updateVisibleCharacters()
            }
        }
    }

    private fun refreshInitial() {
        if (requestInProgress) return

        viewModelScope.launch {
            visibleCount = 10
            nextPage = 1
            requestInProgress = true

            _state.value =
                _state.value.copy(
                    isInitialLoading = true,
                    errorMessage = null,
                    endReached = false,
                )
            try {
                val result = refreshCharactersUseCase(1)
                nextPage = result.nextPage
                updateVisibleCharacters()
                notificationHelper.showRefreshSuccess(1)
            } catch (e: Exception) {
                val savedResult = getSavedCharactersRefreshResultUseCase()
                nextPage = savedResult?.nextPage

                _state.value =
                    _state.value.copy(
                        isInitialLoading = false,
                        errorMessage = e.message ?: "Failed to load data",
                    )
                notificationHelper.showRefreshError(
                    1,
                    e.message ?: "Unknown error",
                )
            } finally {
                requestInProgress = false
            }
        }
    }

    fun loadNextPage() {
        if (requestInProgress) return
        val canShowMoreFromLoaded = visibleCount < allLoadedCharacters.size
        if (canShowMoreFromLoaded) {
            visibleCount += 10
            updateVisibleCharacters()
            return
        }
        val pageToLoad = nextPage ?: return

        viewModelScope.launch {
            requestInProgress = true
            _state.value =
                _state.value.copy(
                    isLoadingMore = true,
                    errorMessage = null,
                )

            try {
                val result = refreshCharactersUseCase(pageToLoad)
                nextPage = result.nextPage
                visibleCount += 10
                updateVisibleCharacters()
                notificationHelper.showRefreshSuccess(pageToLoad)
            } catch (e: Exception) {
                _state.value =
                    _state.value.copy(
                        isLoadingMore = false,
                        errorMessage = e.message ?: "Failed to load more",
                    )
            } finally {
                requestInProgress = false
            }
        }
    }

    private fun updateVisibleCharacters() {
        _state.value =
            _state.value.copy(
                characters = allLoadedCharacters.take(visibleCount),
                isInitialLoading = false,
                isLoadingMore = false,
                errorMessage = null,
                endReached = nextPage == null && visibleCount >= allLoadedCharacters.size,
            )
    }
}
