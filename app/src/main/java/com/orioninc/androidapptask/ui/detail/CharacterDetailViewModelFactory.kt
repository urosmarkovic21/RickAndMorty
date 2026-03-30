package com.orioninc.androidapptask.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.orioninc.androidapptask.domain.GetCharacterUseCase
import com.orioninc.androidapptask.domain.RefreshCharacterUseCase

class CharacterDetailViewModelFactory(
    private val getCharacterUseCase: GetCharacterUseCase,
    private val refreshCharacterUseCase: RefreshCharacterUseCase,
    private val characterId: Int,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterDetailViewModel::class.java)) {
            return CharacterDetailViewModel(
                getCharacterUseCase = getCharacterUseCase,
                refreshCharacterUseCase = refreshCharacterUseCase,
                characterId = characterId,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
