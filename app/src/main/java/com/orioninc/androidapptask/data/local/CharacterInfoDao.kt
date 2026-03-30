package com.orioninc.androidapptask.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterInfoDao {
    @Query("SELECT * FROM character_info WHERE id = 1")
    suspend fun getPageInfo(): CharacterInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageInfo(pageInfo: CharacterInfoEntity)

    @Query("DELETE FROM character_info")
    suspend fun clearPageInfo()
}
