package com.orioninc.androidapptask.domain

import com.orioninc.androidapptask.data.repository.CharacterRepository

class RefreshCharacterUseCase(
    private val repository: CharacterRepository,
) {
    suspend operator fun invoke(id: Int) {
        repository.refreshCharacter(id)
    }
}
