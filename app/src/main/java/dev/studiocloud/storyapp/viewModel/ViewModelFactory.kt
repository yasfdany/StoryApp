package dev.studiocloud.storyapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.studiocloud.storyapp.data.repository.MainRepository
import dev.studiocloud.storyapp.di.Injection

class ViewModelFactory(private val mainRepository: MainRepository?) : ViewModelProvider.Factory {
    companion object{
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(
            context: Context,
        ): ViewModelFactory {
            return INSTANCE ?: synchronized(this){ ViewModelFactory(Injection.provideRepository(context)) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(mainRepository) as T
        } else if(modelClass.isAssignableFrom(StoryViewModel::class.java)){
            return StoryViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}