package dev.studiocloud.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.studiocloud.storyapp.data.model.DefaultResponse
import dev.studiocloud.storyapp.data.model.LoginResponse
import dev.studiocloud.storyapp.data.network.ApiClient
import dev.studiocloud.storyapp.data.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class RemoteRepository {
    private val apiClient: ApiService? = ApiClient().get()
    companion object{
        private var INSTANCE: RemoteRepository? = null

        fun getInstance(): RemoteRepository? {
            if (INSTANCE == null) {
                INSTANCE = RemoteRepository()
            }
            return INSTANCE
        }
    }

    open fun doLogin(
        email: String,
        password: String,
        callback: LoginCallback?,
    ): LiveData<LoginResponse?>{
        val data: MutableLiveData<LoginResponse?> = MutableLiveData()
        val listener = object: Callback<LoginResponse?>{
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if(response.code() == 200){
                    data.value = response.body()
                    callback?.onDataReceived(response.body())
                } else {
                    callback?.onDataNotAvailable(response.body()?.message)
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                callback?.onDataNotAvailable(t.message)
            }
        }

        apiClient?.doLogin(
            email,
            password,
        )?.enqueue(listener)

        return data
    }

    open fun doRegister(
        name: String,
        email: String,
        password: String,
        callback: DefaultCallback?,
    ): LiveData<DefaultResponse?> {
        val data: MutableLiveData<DefaultResponse?> = MutableLiveData()
        val listener = object: Callback<DefaultResponse?> {
            override fun onResponse(
                call: Call<DefaultResponse?>,
                response: Response<DefaultResponse?>
            ) {
                if(response.code() == 200){
                    data.value = response.body()
                    callback?.onDataReceived(response.body())
                } else {
                    callback?.onDataNotAvailable(response.body()?.message)
                }
            }

            override fun onFailure(call: Call<DefaultResponse?>, t: Throwable) {
                callback?.onDataNotAvailable(t.message)
            }
        }

        apiClient?.doRegister(
            name,
            email,
            password,
        )?.enqueue(listener)

        return data
    }

    interface DefaultCallback {
        fun onDataReceived(defaultResponse: DefaultResponse?)
        fun onDataNotAvailable(message: String?)
    }

    interface LoginCallback {
        fun onDataReceived(loginResponse: LoginResponse?)
        fun onDataNotAvailable(message: String?)
    }
}