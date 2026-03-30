package com.orioninc.androidapptask.data.mapper

import com.orioninc.androidapptask.data.local.CharacterInfoEntity
import com.orioninc.androidapptask.data.model.CharactersRefreshResult

fun CharacterInfoEntity.toDomain(): CharactersRefreshResult =
    CharactersRefreshResult(
        nextPage = nextPage,
        isLastPage = isLastPage,
    )

fun CharactersRefreshResult.toEntity(): CharacterInfoEntity =
    CharacterInfoEntity(
        nextPage = nextPage,
        isLastPage = isLastPage,
    )
