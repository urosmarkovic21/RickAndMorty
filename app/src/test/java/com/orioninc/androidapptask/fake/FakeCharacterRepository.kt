package com.orioninc.androidapptask.fake

import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.data.model.CharacterResponse
import com.orioninc.androidapptask.data.model.Info
import com.orioninc.androidapptask.data.repository.CharacterRepository

class FakeCharacterRepository : CharacterRepository {
    var charactersPage1: List<Character> = emptyList()
    var charactersPage2: List<Character> = emptyList()
    var shouldThrowCharactersError = false
    var shouldThrowCharacterError = false
    var character: Character? = null

    override suspend fun getCharacters(page: Int): CharacterResponse {
        if (shouldThrowCharactersError) {
            throw Exception("Network error")
        }

        return CharacterResponse(
            results =
                when (page) {
                    1 -> charactersPage1
                    2 -> charactersPage2
                    else -> emptyList()
                },
            info =
                Info(
                    next = if (page == 1) "next" else null,
                ),
        )
    }

    override suspend fun getCharacter(id: Int): Character {
        if (shouldThrowCharacterError) throw Exception("Error")
        return character ?: throw Exception("No data")
    }
}
