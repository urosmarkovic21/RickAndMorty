package com.orioninc.androidapptask.domain

import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.data.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetCharacterUseCase(
    private val repository: CharacterRepository,
) {
    operator fun invoke(id: Int): Flow<Character?> = repository.getCharacter(id)
}
