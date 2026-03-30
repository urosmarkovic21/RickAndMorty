package com.orioninc.androidapptask.domain

import com.orioninc.androidapptask.data.model.CharactersRefreshResult
import com.orioninc.androidapptask.data.repository.CharacterRepository

class GetSavedCharactersRefreshResultUseCase(
    private val repository: CharacterRepository,
) {
    suspend operator fun invoke(): CharactersRefreshResult? =
        repository.getSavedCharactersRefreshResult()
}
