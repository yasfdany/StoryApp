package dev.studiocloud.storyapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.studiocloud.storyapp.data.MainRepository
import dev.studiocloud.storyapp.data.source.network.model.StoryItem

class StoryViewModel(private val mainRepository: MainRepository?): ViewModel() {
    val stories: MutableLiveData<MutableList<StoryItem>> = MutableLiveData(mutableListOf())
    private var page = 1
    private var endOfPage = false

    fun getStory(
        reset: Boolean = false,
        onFinish: (() -> Unit)? = null,
    ){
        if (endOfPage) {
            onFinish?.invoke()
            return
        }

        mainRepository?.getStory(
            page,
            onSuccess = {
                if(reset){
                    page = 1
                    stories.value?.clear()
                }
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