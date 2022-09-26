package dev.studiocloud.storyapp.di

import android.content.Context
import dev.studiocloud.storyapp.data.repository.MainRepository
import dev.studiocloud.storyapp.data.repository.RemoteRepository
import dev.studiocloud.storyapp.data.source.local.StoryDatabase
import dev.studiocloud.storyapp.data.source.network.ApiClient
import dev.studiocloud.storyapp.data.source.network.ApiService

class Injection {
    companion object{
        fun provideRepository(context: Context): MainRepository? {
            val database: StoryDatabase = StoryDatabase.getDatabase(context)
            val apiService: ApiService? = ApiClient().get()

            val remoteRepository = RemoteRepository.getInstance(database, apiService)
            return MainRepository.getInstance(remoteRepository)
        }
    }
}