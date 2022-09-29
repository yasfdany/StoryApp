package dev.studiocloud.storyapp.data.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryItem

interface MainDataSource {
    fun doLogin(
        email: String,
        password: String,
        onSuccess: (response: LoginResponse?) -> Unit,
        onFailed: ((message: String?) -> Unit)? = null,
    ): LiveData<LoginResponse?>

    fun doRegister(
        name: String,
        email: String,
        password: String,
        onSuccess: (response: DefaultResponse?) -> Unit,
        onFailed: ((message: String?) -> Unit)? = null,
    ): LiveData<DefaultResponse?>

    fun postNewStory(
        photo: Uri?,
        description: String,
        onSuccess: (response: DefaultResponse?) -> Unit,
        onFailed: ((message: String?) -> Unit)? = null,
    ): LiveData<DefaultResponse?>

    fun getStory(): LiveData<PagingData<StoryItem>>

    fun getStoryLocations(
        onSuccess: (response: List<StoryItem>?) -> Unit,
        onFailed: ((message: String?) -> Unit)? = null,
    ): LiveData<List<StoryItem>?>
}