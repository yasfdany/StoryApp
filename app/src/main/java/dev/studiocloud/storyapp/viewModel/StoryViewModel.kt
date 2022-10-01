package dev.studiocloud.storyapp.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.gms.maps.model.LatLng
import dev.studiocloud.storyapp.data.repository.MainRepository
import dev.studiocloud.storyapp.data.source.network.model.StoryItem

class StoryViewModel(private val mainRepository: MainRepository?): ViewModel() {
    val stories: LiveData<PagingData<StoryItem>>? = mainRepository?.getStory()?.cachedIn(viewModelScope)

    fun getStoryLocation() = mainRepository?.getStoryLocations()

    fun postNewStory(
        photo: Uri?,
        description: String,
        latLng: LatLng,
    ) = mainRepository?.postNewStory(
        photo,
        description,
        latLng,
    )
}