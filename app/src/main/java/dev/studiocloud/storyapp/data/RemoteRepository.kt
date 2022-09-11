package dev.studiocloud.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import dev.studiocloud.storyapp.App.Companion.prefs
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.ApiClient
import dev.studiocloud.storyapp.data.source.network.ApiService
import dev.studiocloud.storyapp.data.source.network.model.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Reader

open class RemoteRepository {
    private val apiClient: ApiService? = ApiClient().get()
    private val gson: Gson = Gson()

    companion object{
        private var INSTANCE: RemoteRepository? = null

        fun getInstance(): RemoteRepository? {
            if (INSTANCE == null) {
                INSTANCE = RemoteRepository()
            }
            return INSTANCE
        }
    }

    fun errorBodyToResponse(data: Reader?): DefaultResponse? {
        return gson.fromJson(data, DefaultResponse::class.java)
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
                if(response.isSuccessful){
                    data.value = response.body()
                    callback?.onDataReceived(response.body())
                } else {
                    val errorResponse: DefaultResponse? = errorBodyToResponse(response.errorBody()?.charStream())
                    callback?.onDataNotAvailable(errorResponse?.message)
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
                if(response.isSuccessful){
                    data.value = response.body()
                    callback?.onDataReceived(response.body())
                } else {
                    val errorResponse: DefaultResponse? = errorBodyToResponse(response.errorBody()?.charStream())
                    callback?.onDataNotAvailable(errorResponse?.message)
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

    open fun getStory(
        page: Int,
        callback: StoryCallback?
    ): LiveData<StoryResponse?>{
        val token = prefs?.user?.token
        val data: MutableLiveData<StoryResponse?> = MutableLiveData()
        val listener = object: Callback<StoryResponse?> {
            override fun onResponse(
                call: Call<StoryResponse?>,
                response: Response<StoryResponse?>
            ) {
                if(response.isSuccessful){
                    data.value = response.body()
                    callback?.onDataReceived(response.body())
                } else {
                    val errorResponse: DefaultResponse? = errorBodyToResponse(response.errorBody()?.charStream())
                    callback?.onDataNotAvailable(errorResponse?.message)
                }
            }

            override fun onFailure(call: Call<StoryResponse?>, t: Throwable) {
                callback?.onDataNotAvailable(t.message)
            }
        }

        apiClient?.getAllStories(
            Authorization = "Bearer $token",
            page,
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

    interface StoryCallback {
        fun onDataReceived(storyResponse: StoryResponse?)
        fun onDataNotAvailable(message: String?)
    }
}