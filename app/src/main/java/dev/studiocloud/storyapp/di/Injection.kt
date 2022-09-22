package dev.studiocloud.storyapp.di

import dev.studiocloud.storyapp.data.MainRepository
import dev.studiocloud.storyapp.data.RemoteRepository

class Injection {
    companion object{
        fun provideRepository(): MainRepository? {
            val remoteRepository = RemoteRepository.getInstance()
            return MainRepository.getInstance(remoteRepository)
        }
    }
}