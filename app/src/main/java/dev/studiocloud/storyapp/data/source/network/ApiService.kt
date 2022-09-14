package dev.studiocloud.storyapp.data.source.network

import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    fun getAllStories(
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Call<StoryResponse>?

    @FormUrlEncoded
    @POST("register")
    fun doRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<DefaultResponse?>?

    @FormUrlEncoded
    @POST("login")
    fun doLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse?>?

    @Multipart
    @POST("stories")
    fun postNewStory(
        @Header("Authorization") Authorization: String,
        @Part("description") description: String,
        @Part photo: MultipartBody.Part
    ): Call<DefaultResponse?>?
}