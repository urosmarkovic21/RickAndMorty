package com.orioninc.androidapptask.data.network

import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.data.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApi {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
    ): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int,
    ): Character
}
