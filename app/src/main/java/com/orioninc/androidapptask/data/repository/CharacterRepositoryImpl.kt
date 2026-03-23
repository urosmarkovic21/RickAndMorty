package com.orioninc.androidapptask.data.repository

import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.data.model.CharacterResponse
import com.orioninc.androidapptask.data.network.CharacterApi

class CharacterRepositoryImpl(private val api: CharacterApi) : CharacterRepository {

    override suspend fun getCharacters(page: Int): CharacterResponse = api.getCharacters(page)

    override suspend fun getCharacter(id: Int): Character = api.getCharacter(id)
}