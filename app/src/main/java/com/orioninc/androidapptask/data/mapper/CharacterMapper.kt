package com.orioninc.androidapptask.data.mapper

import com.orioninc.androidapptask.data.local.CharacterEntity
import com.orioninc.androidapptask.data.model.Character

fun Character.toEntity(page: Int): CharacterEntity =
    CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        page = page,
    )

fun CharacterEntity.toDomain(): Character =
    Character(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
    )
