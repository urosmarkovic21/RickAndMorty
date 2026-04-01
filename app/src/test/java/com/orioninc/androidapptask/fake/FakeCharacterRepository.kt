package com.orioninc.androidapptask.fake

import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.data.model.CharactersRefreshResult
import com.orioninc.androidapptask.data.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCharacterRepository : CharacterRepository {
    var characters: List<Character> = emptyList()

    var savedRefreshResult: CharactersRefreshResult? =
        CharactersRefreshResult(
            nextPage = 2,
            isLastPage = false,
        )

    var shouldThrowCharactersError = false
    var shouldThrowCharacterError = false

    var character: Character? = null

    override fun getCharacters(): Flow<List<Character>> = flowOf(characters)

    override suspend fun refreshCharacters(page: Int): CharactersRefreshResult {
        if (shouldThrowCharactersError) {
            throw Exception("Network error")
        }

        return savedRefreshResult ?: CharactersRefreshResult(
            nextPage = null,
            isLastPage = true,
        )
    }

    override fun getCharacter(id: Int): Flow<Character?> {
        if (shouldThrowCharacterError) {
            return flowOf(null)
        }

        return flowOf(character)
    }

    override suspend fun refreshCharacter(id: Int) {
        if (shouldThrowCharacterError) {
            throw Exception("Error")
        }
    }

    override suspend fun getSavedCharactersRefreshResult(): CharactersRefreshResult? =
        savedRefreshResult
}
