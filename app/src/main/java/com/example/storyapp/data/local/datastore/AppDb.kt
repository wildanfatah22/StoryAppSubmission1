package com.example.storyapp.data.local.datastore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storyapp.data.local.entity.RemoteKeys
import com.example.storyapp.data.local.entity.StoryDetailResponse
import com.example.storyapp.data.local.room.RemoteKeysDao
import com.example.storyapp.data.local.room.StoryDetailResponseDao

@Database(
    entities = [StoryDetailResponse::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb: RoomDatabase() {
    abstract fun storyDetailResponseDao():StoryDetailResponseDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        @JvmStatic
        fun getDatabase(context: Context): AppDb {
            if (INSTANCE == null) {
                synchronized(AppDb::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDb::class.java,
                        "story_database"
                    ).build()
                }
            }
            return INSTANCE as AppDb
        }
    }
}