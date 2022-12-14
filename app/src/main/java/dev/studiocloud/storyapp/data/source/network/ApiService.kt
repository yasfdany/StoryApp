package dev.studiocloud.storyapp.data.source.network

import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<StoryResponse>

    @GET("stories?location=1")
    suspend fun getStoryLocations(
        @Header("Authorization") Authorization: String,
    ): Response<StoryResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun doRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<DefaultResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun doLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginResponse>

    @Multipart
    @POST("stories")
    suspend fun postNewStory(
        @Header("Authorization") Authorization: String,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part photo: MultipartBody.Part
    ): Response<DefaultResponse>
}