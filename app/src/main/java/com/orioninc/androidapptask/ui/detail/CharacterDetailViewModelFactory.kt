package com.orioninc.androidapptask.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.orioninc.androidapptask.data.repository.CharacterRepositoryImpl

class CharacterDetailViewModelFactory(
    private val repository: CharacterRepositoryImpl,
    private val characterId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterDetailViewModel(repository, characterId) as T
    }
}