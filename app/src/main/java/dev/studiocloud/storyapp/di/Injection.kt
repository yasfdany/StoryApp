package dev.studiocloud.storyapp.di

import android.app.Application
import dev.studiocloud.storyapp.data.MainRepository
import dev.studiocloud.storyapp.data.RemoteRepository

class Injection {
    companion object{
        fun provideRepository(application: Application): MainRepository? {
            val remoteRepository = RemoteRepository.getInstance()
            return MainRepository.getInstance(remoteRepository!!)
        }
    }
}