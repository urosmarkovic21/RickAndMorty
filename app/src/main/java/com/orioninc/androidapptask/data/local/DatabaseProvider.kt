package com.orioninc.androidapptask.data.local
import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase =
        instance ?: synchronized(this) {
            val instance =
                Room
                    .databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database",
                    ).fallbackToDestructiveMigration()
                    .build()

            DatabaseProvider.instance = instance
            instance
        }
}
