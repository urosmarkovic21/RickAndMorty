package com.orioninc.androidapptask.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.orioninc.androidapptask.data.repository.CharacterRepository

class CharacterDetailViewModelFactory(
    private val repository: CharacterRepository,
    private val characterId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterDetailViewModel(repository, characterId) as T
    }
}