package dev.studiocloud.storyapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.studiocloud.storyapp.data.repository.MainRepository

class ViewModelFactory(private val mainRepository: MainRepository?) : ViewModelProvider.Factory {
    companion object{
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(mainRepository: MainRepository?): ViewModelFactory? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelFactory(mainRepository)
            }
            return INSTANCE
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}