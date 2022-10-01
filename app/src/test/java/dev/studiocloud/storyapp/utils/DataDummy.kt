package dev.studiocloud.storyapp.utils

import com.google.gson.Gson
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse

object DataDummy {
    val loginSuccess: LoginResponse = Gson().fromJson(
        "{\"error\":false,\"message\":\"success\",\"loginResult\":{\"userId\":\"user-RBCBHlZuTeQFWnxK\",\"name\":\"Test Case\",\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVJCQ0JIbFp1VGVRRldueEsiLCJpYXQiOjE2NjQ2MTQyMzh9.tUL-I9UEAjtbXb2e-kOvBTONhY0HDBLjZqZjUTg3qt0\"}}",
        LoginResponse::class.java,
    )
    val errorResponse: DefaultResponse = Gson().fromJson(
        "{\"error\":true,\"message\":\"Invalid password\"}",
        DefaultResponse::class.java
    )
}