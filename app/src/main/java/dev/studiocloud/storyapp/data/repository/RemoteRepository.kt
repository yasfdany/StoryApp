package dev.studiocloud.storyapp.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import dev.studiocloud.storyapp.App.Companion.prefs
import dev.studiocloud.storyapp.data.ResultData
import dev.studiocloud.storyapp.data.mediator.StoryRemoteMediator
import dev.studiocloud.storyapp.data.source.local.StoryDatabase
import dev.studiocloud.storyapp.data.source.network.ApiService
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
    ): LiveData<ResultData<LoginResponse?>> = liveData {
        emit(ResultData.Loading)
        try {
            val response = apiService?.doLogin(
                email,
                password,
            )
            if (response?.isSuccessful == true){
                prefs?.user = response.body()?.loginResult
                emit(ResultData.Success(response.body()))
            } else {
                val errorResponse: DefaultResponse? = errorBodyToResponse(response?.errorBody()?.charStream())
                emit(ResultData.Error(errorResponse?.message.toString()))
            }
        } catch (e: Exception){
            emit(ResultData.Error(e.message.toString()))
        }
    }

    open fun doRegister(
        name: String,
        email: String,
        password: String,
    ): LiveData<ResultData<DefaultResponse?>> = liveData {
        emit(ResultData.Loading)
        try {
            val response = apiService?.doRegister(
                name,
                email,
                password,
            )
            if (response?.isSuccessful == true){
                emit(ResultData.Success(response.body()))
            } else {
                val errorResponse: DefaultResponse? = errorBodyToResponse(response?.errorBody()?.charStream())
                emit(ResultData.Error(errorResponse?.message.toString()))
            }
        } catch (e: Exception){
            emit(ResultData.Error(e.message.toString()))
        }
    }

    open fun postNewStory(
        photo: Uri?,
        description: String,
        latLng: LatLng,
    ): LiveData<ResultData<DefaultResponse?>> = liveData {
        emit(ResultData.Loading)
        val token = prefs?.user?.token

        try {
            if(photo?.path != null){
                val file = File(photo.path!!)

                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("photo", file.name, requestFile)
                val descriptionBody: RequestBody =
                    description.toRequestBody("text/plain".toMediaTypeOrNull())
                val latitudeBody: RequestBody =
                    latLng.latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val longitudeBody: RequestBody =
                    latLng.longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                val response = apiService?.postNewStory(
                    Authorization = "Bearer $token",
                    description = descriptionBody,
                    lat = latitudeBody,
                    lon = longitudeBody,
                    photo = body,
                )
                if (response?.isSuccessful == true){
                    emit(ResultData.Success(response.body()))
                } else {
                    val errorResponse: DefaultResponse? = errorBodyToResponse(response?.errorBody()?.charStream())
                    emit(ResultData.Error(errorResponse?.message.toString()))
                }
            } else {
                emit(ResultData.Error("File not found"))
            }
        } catch (e: Exception){
            emit(ResultData.Error(e.message.toString()))
        }
    }

    open fun getStoryLocations(): LiveData<ResultData<List<StoryItem>?>> = liveData {
        emit(ResultData.Loading)
        val token = prefs?.user?.token

        try {
            val response = apiService?.getStoryLocations("Bearer $token")
            if (response?.isSuccessful == true){
                emit(ResultData.Success(response.body()?.listStory))
            } else {
                val errorResponse: DefaultResponse? = errorBodyToResponse(response?.errorBody()?.charStream())
                emit(ResultData.Error(errorResponse?.message.toString()))
            }
        } catch (e: Exception){
            emit(ResultData.Error(e.message.toString()))
        }
    }
}