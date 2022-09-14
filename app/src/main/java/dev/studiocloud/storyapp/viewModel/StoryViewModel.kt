package dev.studiocloud.storyapp.viewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.studiocloud.storyapp.data.MainRepository
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryItem

class StoryViewModel(private val mainRepository: MainRepository?): ViewModel() {
    val stories: MutableLiveData<MutableList<StoryItem>> = MutableLiveData(mutableListOf())
    private var page = 1
    private var endOfPage = false

    fun postNewStory(
        photo: Uri?,
        description: String,
        onSuccess: (response: DefaultResponse?) -> Unit,
        onFailed: ((message: String?) -> Unit)? = null,
    ){
        mainRepository?.postNewStory(
            photo,
            description,
            onSuccess = {
                getStory(true)
                onSuccess(it)
            },
            onFailed,
        )
    }

    fun getStory(
        reset: Boolean = false,
        onFinish: (() -> Unit)? = null,
    ){
        if(reset){
            endOfPage = false
            page = 1
            stories.value?.clear()
        }

        if (endOfPage) {
            onFinish?.invoke()
            return
        }

        mainRepository?.getStory(
            page,
            onSuccess = {
                stories.value?.addAll(it?.listStory ?: mutableListOf())
                stories.value = stories.value

                page++
                endOfPage = it?.listStory?.isEmpty() == true
                onFinish?.invoke()
            },
            onFailed = {
                onFinish?.invoke()
            }
        )
    }
}