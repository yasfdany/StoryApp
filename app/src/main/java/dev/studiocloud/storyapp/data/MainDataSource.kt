package dev.studiocloud.storyapp.data

import androidx.lifecycle.LiveData
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryResponse

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

    fun getStory(
        page: Int,
        onSuccess: (response: StoryResponse?) -> Unit,
        onFailed: ((message: String?) -> Unit)? = null,
    ): LiveData<StoryResponse?>
}