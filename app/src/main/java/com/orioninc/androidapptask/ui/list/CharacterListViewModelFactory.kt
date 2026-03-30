package com.orioninc.androidapptask.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.orioninc.androidapptask.domain.GetCharactersUseCase
import com.orioninc.androidapptask.domain.GetSavedCharactersRefreshResultUseCase
import com.orioninc.androidapptask.domain.RefreshCharactersUseCase
import com.orioninc.androidapptask.util.NotificationHelper

class CharacterListViewModelFactory(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val refreshCharactersUseCase: RefreshCharactersUseCase,
    private val getSavedCharactersRefreshResultUseCase: GetSavedCharactersRefreshResultUseCase,
    private val notificationHelper: NotificationHelper,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterListViewModel::class.java)) {
            return CharacterListViewModel(
                getCharactersUseCase = getCharactersUseCase,
                refreshCharactersUseCase = refreshCharactersUseCase,
                getSavedCharactersRefreshResultUseCase = getSavedCharactersRefreshResultUseCase,
                notificationHelper = notificationHelper,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
