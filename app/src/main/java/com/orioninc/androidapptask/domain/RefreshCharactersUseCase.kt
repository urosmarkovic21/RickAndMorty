package com.orioninc.androidapptask.domain

import com.orioninc.androidapptask.data.model.CharactersRefreshResult
import com.orioninc.androidapptask.data.repository.CharacterRepository

class RefreshCharactersUseCase(
    private val repository: CharacterRepository,
) {
    suspend operator fun invoke(page: Int): CharactersRefreshResult =
        repository.refreshCharacters(page)
}
