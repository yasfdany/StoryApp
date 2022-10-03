package dev.studiocloud.storyapp.data

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import dev.studiocloud.storyapp.data.source.local.RemoteKeysDao
import dev.studiocloud.storyapp.data.source.local.StoryDao
import dev.studiocloud.storyapp.data.source.local.StoryDatabase
import org.mockito.Mockito

class FakeStoryDatabase: StoryDatabase() {
    override fun storyDao(): StoryDao = FakeStoryDao()
    override fun remoteKeysDao(): RemoteKeysDao = FakeRemoteKeysDao()
    override fun createInvalidationTracker(): InvalidationTracker {
        return Mockito.mock(InvalidationTracker::class.java)
    }
    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        return Mockito.mock(SupportSQLiteOpenHelper::class.java)
    }
    override fun clearAllTables() {}
}