package dev.studiocloud.storyapp.data.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.google.android.gms.maps.model.LatLng
import dev.studiocloud.storyapp.data.ResultData
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryItem

interface MainDataSource {
    fun doLogin(
        email: String,
        password: String,
    ): LiveData<ResultData<LoginResponse?>>

    fun doRegister(
        name: String,
        email: String,
        password: String,
    ): LiveData<ResultData<DefaultResponse?>>

    fun postNewStory(
        photo: Uri?,
        description: String,
        latLng: LatLng,
    ): LiveData<ResultData<DefaultResponse?>>

    fun getStory(): LiveData<PagingData<StoryItem>>

    fun getStoryLocations(): LiveData<ResultData<List<StoryItem>?>>
}