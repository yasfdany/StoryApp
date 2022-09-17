package dev.studiocloud.storyapp.data

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import dev.studiocloud.storyapp.App.Companion.prefs
import dev.studiocloud.storyapp.data.source.network.ApiClient
import dev.studiocloud.storyapp.data.source.network.ApiService
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
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
            size = 10,
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

            apiClient?.postNewStory(
                Authorization = "Bearer $token",
                description = descriptionBody,
                photo = body,
            )?.enqueue(listener)
        }

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