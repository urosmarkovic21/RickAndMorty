package com.orioninc.androidapptask.data.repository

import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.data.model.CharacterResponse
import com.orioninc.androidapptask.data.network.CharacterApi

class CharacterRepository(val api: CharacterApi) {

    suspend fun getCharacters(): CharacterResponse = api.getCharacters()

    suspend fun getCharacter(id: Int): Character = api.getCharacter(id)
}