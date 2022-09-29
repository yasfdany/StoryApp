package dev.studiocloud.storyapp.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.google.gson.Gson
import dev.studiocloud.storyapp.App.Companion.prefs
import dev.studiocloud.storyapp.data.mediator.StoryRemoteMediator
import dev.studiocloud.storyapp.data.source.local.StoryDatabase
import dev.studiocloud.storyapp.data.source.network.ApiService
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
import dev.studiocloud.storyapp.data.source.network.model.StoryResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.Reader

open class RemoteRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService?
) {
    private val gson: Gson = Gson()

    companion object{
        private var INSTANCE: RemoteRepository? = null

        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService?
        ): RemoteRepository {
            return INSTANCE ?: synchronized(this){
                RemoteRepository(storyDatabase, apiService)
            }
        }
    }

    fun errorBodyToResponse(data: Reader?): DefaultResponse? {
        return gson.fromJson(data, DefaultResponse::class.java)
    }

    @OptIn(ExperimentalPagingApi::class)
    open fun getStory(): LiveData<PagingData<StoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
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

        apiService?.doLogin(
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

        apiService?.doRegister(
            name,
            email,
            password,
        )?.enqueue(listener)

        return data
    }

    open fun postNewStory(
        photo: Uri?,
        description: String,
        callback: DefaultCallback?,
    ): LiveData<DefaultResponse?> {
        val token = prefs?.user?.token
        val data: MutableLiveData<DefaultResponse?> = MutableLiveData()
        val listener = object: Callback<DefaultResponse?>{
            override fun onResponse(
                call: Call<DefaultResponse?>,
                response: Response<DefaultResponse?>
            ) {
                if (response.isSuccessful){
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

        if(photo?.path != null){
            val file = File(photo.path!!)

            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("photo", file.name, requestFile)
            val descriptionBody: RequestBody =
                description.toRequestBody("text/plain".toMediaTypeOrNull())

            apiService?.postNewStory(
                Authorization = "Bearer $token",
                description = descriptionBody,
                photo = body,
            )?.enqueue(listener)
        }

        return data
    }

    open fun getStoryLocations(callback: StoryCallback?): LiveData<List<StoryItem>?>{
        val token = prefs?.user?.token
        val data = MutableLiveData<List<StoryItem>?>()
        val listener = object: Callback<StoryResponse?>{
            override fun onResponse(call: Call<StoryResponse?>, response: Response<StoryResponse?>) {
                if (response.isSuccessful){
                    callback?.onDataReceived(response.body())
                    data.value = response.body()?.listStory
                } else {
                    val errorResponse: DefaultResponse? = errorBodyToResponse(response.errorBody()?.charStream())
                    callback?.onDataNotAvailable(errorResponse?.message)
                }
            }

            override fun onFailure(call: Call<StoryResponse?>, t: Throwable) {
                callback?.onDataNotAvailable(t.message)
            }
        }

        apiService?.getStoryLocations("Bearer $token")?.enqueue(listener)
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