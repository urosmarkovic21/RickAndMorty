package com.orioninc.androidapptask.domain

import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.data.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase(
    private val repository: CharacterRepository,
) {
    operator fun invoke(): Flow<List<Character>> = repository.getCharacters()
}
