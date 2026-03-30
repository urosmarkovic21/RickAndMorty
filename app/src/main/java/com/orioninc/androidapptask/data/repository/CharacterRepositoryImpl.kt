package com.orioninc.androidapptask.data.repository

import com.orioninc.androidapptask.data.local.CharacterDao
import com.orioninc.androidapptask.data.local.CharacterInfoDao
import com.orioninc.androidapptask.data.local.CharacterInfoEntity
import com.orioninc.androidapptask.data.mapper.toDomain
import com.orioninc.androidapptask.data.mapper.toEntity
import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.data.model.CharactersRefreshResult
import com.orioninc.androidapptask.data.network.CharacterApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterRepositoryImpl(
    private val api: CharacterApi,
    private val dao: CharacterDao,
    private val infoDao: CharacterInfoDao,
) : CharacterRepository {
    override fun getCharacters(): Flow<List<Character>> =
        dao.getAllCharacters().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getCharacter(id: Int): Flow<Character?> =
        dao.getCharacterById(id).map { entity ->
            entity?.toDomain()
        }

    override suspend fun refreshCharacters(page: Int): CharactersRefreshResult {
        val response = api.getCharacters(page)
        dao.insertAll(response.results.map { it.toEntity(page) })

        val nextPage = extractNextPage(response.info.next)
        val result =
            CharactersRefreshResult(
                nextPage = nextPage,
                isLastPage = nextPage == null,
            )
        infoDao.insertPageInfo(
            CharacterInfoEntity(
                nextPage = result.nextPage,
                isLastPage = result.isLastPage,
            ),
        )

        return result
    }

    override suspend fun refreshCharacter(id: Int) {
        val character = api.getCharacter(id)
        dao.insertCharacter(character.toEntity(page = -1))
    }

    private fun extractNextPage(nextUrl: String?): Int? =
        nextUrl
            ?.substringAfter("page=", "")
            ?.substringBefore("&")
            ?.toIntOrNull()

    override suspend fun getSavedCharactersRefreshResult(): CharactersRefreshResult? =
        infoDao.getPageInfo()?.toDomain()
}
