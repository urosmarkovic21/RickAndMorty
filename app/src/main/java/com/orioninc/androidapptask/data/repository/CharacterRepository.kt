package com.orioninc.androidapptask.data.repository

import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.data.model.CharactersRefreshResult
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(): Flow<List<Character>>

    fun getCharacter(id: Int): Flow<Character?>

    suspend fun refreshCharacters(page: Int): CharactersRefreshResult

    suspend fun refreshCharacter(id: Int)

    suspend fun getSavedCharactersRefreshResult(): CharactersRefreshResult?
}
