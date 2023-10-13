package com.example.storyapp.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.data.local.entity.StoryDetailResponse

@Dao
interface StoryDetailResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<StoryDetailResponse>)

    @Query("SELECT * FROM stories")
    fun getAllStories(): PagingSource<Int, StoryDetailResponse>

    @Query("SELECT * FROM stories")
    fun getAllListStories(): List<StoryDetailResponse>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}