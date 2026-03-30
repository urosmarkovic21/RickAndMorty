package com.orioninc.androidapptask.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_info")
data class CharacterInfoEntity(
    @PrimaryKey val id: Int = 1,
    val nextPage: Int?,
    val isLastPage: Boolean,
)
