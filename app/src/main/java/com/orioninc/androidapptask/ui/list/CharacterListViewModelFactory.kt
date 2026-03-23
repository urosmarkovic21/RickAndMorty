package com.orioninc.androidapptask.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.orioninc.androidapptask.data.repository.CharacterRepositoryImpl

class CharacterListViewModelFactory(
    private val repository: CharacterRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterListViewModel(repository) as T
    }
}