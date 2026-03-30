package com.orioninc.androidapptask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import kotlin.random.Random

@Database(
    entities = [
        CharacterEntity::class,
        CharacterInfoEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    abstract fun characterInfoDao(): CharacterInfoDao
}
