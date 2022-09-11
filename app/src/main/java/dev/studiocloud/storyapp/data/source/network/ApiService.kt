package dev.studiocloud.storyapp.data.source.network

import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    fun getAllStories(
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 10,
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
}