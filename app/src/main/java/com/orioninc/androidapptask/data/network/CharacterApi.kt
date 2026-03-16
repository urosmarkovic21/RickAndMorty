package com.orioninc.androidapptask.data.network

import com.orioninc.androidapptask.data.model.CharacterResponse
import com.orioninc.androidapptask.data.model.Character
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterApi {

    @GET("character")
    suspend fun getCharacters(): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Character
}