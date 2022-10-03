package dev.studiocloud.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.studiocloud.storyapp.data.source.local.StoryDao
import dev.studiocloud.storyapp.data.source.network.model.StoryItem

class FakeStoryDao: StoryDao {
    private val storyData = mutableListOf<StoryItem>()

    override suspend fun insertStory(stories: List<StoryItem>) {
        storyData.addAll(stories)
    }

    override fun getAllStory(): PagingSource<Int, StoryItem> {
        return FakePagingSource(storyData)
    }

    override suspend fun deleteAll() {
        storyData.clear()
    }

    class FakePagingSource(private val data: MutableList<StoryItem>): PagingSource<Int, StoryItem>(){
        @Suppress("SameReturnValue")
        override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int = 0

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
            return LoadResult.Page(data, 0, 1)
        }

    }
}