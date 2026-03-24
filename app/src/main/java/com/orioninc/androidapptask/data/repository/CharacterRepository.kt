package com.orioninc.androidapptask.data.repository

import com.orioninc.androidapptask.data.model.CharacterResponse
import com.orioninc.androidapptask.data.model.Character

interface CharacterRepository {
    suspend fun getCharacters(page: Int): CharacterResponse
    suspend fun getCharacter(id: Int): Character
}