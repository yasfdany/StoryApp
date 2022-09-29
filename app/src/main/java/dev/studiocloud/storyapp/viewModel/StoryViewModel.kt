package dev.studiocloud.storyapp.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.studiocloud.storyapp.data.repository.MainRepository
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryItem

class StoryViewModel(private val mainRepository: MainRepository?): ViewModel() {
    val stories: LiveData<PagingData<StoryItem>>? = mainRepository?.getStory()?.cachedIn(viewModelScope)

    fun getStoryLocation(
        onSuccess: (response: List<StoryItem>?) -> Unit,
        onFailed: ((message: String?) -> Unit)? = null,
    ){
        mainRepository?.getStoryLocations(
            onSuccess,
            onFailed
        )
    }

    fun postNewStory(
        photo: Uri?,
        description: String,
        onSuccess: (response: DefaultResponse?) -> Unit,
        onFailed: ((message: String?) -> Unit)? = null,
    ){
        mainRepository?.postNewStory(
            photo,
            description,
            onSuccess = { onSuccess(it) },
            onFailed,
        )
    }
}