package dev.studiocloud.storyapp.di

import dev.studiocloud.storyapp.data.MainRepository
import dev.studiocloud.storyapp.data.RemoteRepository
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

object Injection {
    fun provideRepository(): MainRepository? {
        val remoteRepository = RemoteRepository.getInstance()
        return MainRepository.getInstance(remoteRepository!!)
    }

    fun provideViewModelFactory() = ViewModelFactory.getInstance()
}