package dev.studiocloud.storyapp.data.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.studiocloud.storyapp.data.source.network.model.StoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<StoryItem>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, StoryItem>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}