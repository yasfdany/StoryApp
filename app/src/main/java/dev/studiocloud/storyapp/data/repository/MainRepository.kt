package dev.studiocloud.storyapp.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.google.android.gms.maps.model.LatLng
import dev.studiocloud.storyapp.data.source.MainDataSource
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
import dev.studiocloud.storyapp.data.source.network.model.StoryResponse

class MainRepository(
    private val remoteRepository: RemoteRepository,
): MainDataSource {
    companion object{
        private var INSTANCE: MainRepository? = null

        fun getInstance(
            remoteRepository: RemoteRepository,
        ): MainRepository? {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = MainRepository(
                            remoteRepository,
                        )
                    }
                }
            }

            return INSTANCE
        }
    }

    override fun doLogin(
        email: String,
        password: String,
        onSuccess: (response: LoginResponse?) -> Unit,
        onFailed: ((message: String?) -> Unit)?
    ): LiveData<LoginResponse?> {
        val response: MutableLiveData<LoginResponse?> = MutableLiveData()

        remoteRepository.doLogin(email, password, object: RemoteRepository.LoginCallback {
                override fun onDataReceived(loginResponse: LoginResponse?) {
                    onSuccess(loginResponse)
                }
                override fun onDataNotAvailable(message: String?) {
                    onFailed?.invoke(message)
                }
            }
        )

        return response
    }

    override fun doRegister(
        name: String,
        email: String,
        password: String,
        onSuccess: (response: DefaultResponse?) -> Unit,
        onFailed: ((message: String?) -> Unit)?
    ): LiveData<DefaultResponse?> {
        val response: MutableLiveData<DefaultResponse?> = MutableLiveData()

        remoteRepository.doRegister(name, email, password, object:
            RemoteRepository.DefaultCallback {
            override fun onDataReceived(defaultResponse: DefaultResponse?) {
                onSuccess(defaultResponse)
            }

            override fun onDataNotAvailable(message: String?) {
                onFailed?.invoke(message)
            }
        })

        return response
    }

    override fun postNewStory(
        photo: Uri?,
        description: String,
        latLng: LatLng,
        onSuccess: (response: DefaultResponse?) -> Unit,
        onFailed: ((message: String?) -> Unit)?
    ): LiveData<DefaultResponse?> {
        val response: MutableLiveData<DefaultResponse?> = MutableLiveData()

        remoteRepository.postNewStory(
            photo,
            description,
            latLng,
            object : RemoteRepository.DefaultCallback {
                override fun onDataReceived(defaultResponse: DefaultResponse?) {
                    onSuccess(defaultResponse)
                }

                override fun onDataNotAvailable(message: String?) {
                    onFailed?.invoke(message)
                }
            }
        )

        return response
    }

    override fun getStory(): LiveData<PagingData<StoryItem>> {
        return remoteRepository.getStory()
    }

    override fun getStoryLocations(
        onSuccess: (data: List<StoryItem>?) -> Unit,
        onFailed: ((message: String?) -> Unit)?
    ): LiveData<List<StoryItem>?> {
        val data = MutableLiveData<List<StoryItem>?>()

        remoteRepository.getStoryLocations(object: RemoteRepository.StoryCallback{
            override fun onDataReceived(storyResponse: StoryResponse?) {
                data.value = storyResponse?.listStory
                onSuccess(data.value)
            }

            override fun onDataNotAvailable(message: String?) {
                onFailed?.invoke(message)
            }
        })

        return data
    }
}