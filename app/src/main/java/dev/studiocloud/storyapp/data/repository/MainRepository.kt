package dev.studiocloud.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.studiocloud.storyapp.data.MainDataSource
import dev.studiocloud.storyapp.data.model.DefaultResponse
import dev.studiocloud.storyapp.data.model.LoginResponse

class MainRepository(
    private val remoteRepository: RemoteRepository,
): MainDataSource {
    companion object{
        private var INSTANCE: MainRepository? = null

        fun getInstance(
            remoteRepository: RemoteRepository,
        ): MainRepository? {
            if (INSTANCE == null) {
                synchronized(RemoteRepository::class.java) {
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
        onLoginSuccess: (response: LoginResponse?) -> Unit,
        onLoginFailed: ((message: String?) -> Unit)?
    ): LiveData<LoginResponse?> {
        val response: MutableLiveData<LoginResponse?> = MutableLiveData()

        remoteRepository.doLogin(email, password, object: RemoteRepository.LoginCallback{
                override fun onDataReceived(loginResponse: LoginResponse?) {
                    onLoginSuccess(loginResponse)
                }
                override fun onDataNotAvailable(message: String?) {
                    onLoginFailed?.invoke(message)
                }
            }
        )

        return response
    }

    override fun doRegister(
        name: String,
        email: String,
        password: String,
        onRegisterSuccess: (response: DefaultResponse?) -> Unit,
        onRegisterFailed: ((message: String?) -> Unit)?
    ): LiveData<DefaultResponse?> {
        val response: MutableLiveData<DefaultResponse?> = MutableLiveData()

        remoteRepository.doRegister(name, email, password, object: RemoteRepository.DefaultCallback{
            override fun onDataReceived(defaultResponse: DefaultResponse?) {
                onRegisterSuccess(defaultResponse)
            }

            override fun onDataNotAvailable(message: String?) {
                onRegisterFailed?.invoke(message)
            }
        })

        return response
    }
}