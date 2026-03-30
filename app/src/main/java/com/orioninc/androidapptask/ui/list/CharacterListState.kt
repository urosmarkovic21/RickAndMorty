package com.orioninc.androidapptask.ui.list

import com.orioninc.androidapptask.data.model.Character

data class CharacterListState(
    val characters: List<Character> = emptyList(),
    val isInitialLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val endReached: Boolean = false,
)
