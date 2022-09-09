package dev.studiocloud.storyapp.data.services

import dev.studiocloud.storyapp.data.services.responses.DefaultResponse
import dev.studiocloud.storyapp.data.services.responses.LoginResponse
import dev.studiocloud.storyapp.data.services.responses.StoryResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/stories")
    fun getAllStories(
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int,
        @Query("page") size: Int = 10,
    ): Call<StoryResponse>?

    @FormUrlEncoded
    @POST("/register")
    fun doRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<DefaultResponse?>?

    @FormUrlEncoded
    @POST("/login")
    fun doLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse?>?
}