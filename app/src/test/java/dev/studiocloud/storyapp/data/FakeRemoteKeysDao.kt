package dev.studiocloud.storyapp.data

import dev.studiocloud.storyapp.data.source.local.RemoteKeysDao
import dev.studiocloud.storyapp.data.source.local.entity.RemoteKeys

class FakeRemoteKeysDao: RemoteKeysDao {
    private val remoteKeys = mutableListOf<RemoteKeys>()

    override suspend fun insertAll(remoteKey: List<RemoteKeys>) {
        remoteKeys.addAll(remoteKey)
    }

    override suspend fun getRemoteKeysId(id: String): RemoteKeys? {
        return remoteKeys.firstOrNull { it.id == id }
    }

    override suspend fun deleteRemoteKeys() {
        remoteKeys.clear()
    }
}