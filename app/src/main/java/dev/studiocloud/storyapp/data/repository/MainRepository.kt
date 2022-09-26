package dev.studiocloud.storyapp.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import dev.studiocloud.storyapp.data.source.MainDataSource
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryItem

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
        onSuccess: (response: DefaultResponse?) -> Unit,
        onFailed: ((message: String?) -> Unit)?
    ): LiveData<DefaultResponse?> {
        val response: MutableLiveData<DefaultResponse?> = MutableLiveData()

        remoteRepository.postNewStory(
            photo,
            description,
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
}