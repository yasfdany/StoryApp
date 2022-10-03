package dev.studiocloud.storyapp.data

import dev.studiocloud.storyapp.data.source.network.ApiService
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryResponse
import dev.studiocloud.storyapp.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class FakeApiService: ApiService {
    override suspend fun getAllStories(
        Authorization: String,
        page: Int,
        size: Int
    ): Response<StoryResponse> {
        val storyResponse =
            if(size == 0) DataDummy.getEmptyStoryResponse
            else DataDummy.getStoryResponse
        return Response.success(storyResponse)
    }

    override suspend fun getStoryLocations(Authorization: String): Response<StoryResponse> {
        return if (Authorization == "token") {
            Response.success(DataDummy.getStoryWithLocationResponse)
        } else {
            Response.success(DataDummy.getEmptyStoryResponse)
        }
    }

    override suspend fun doRegister(
        name: String,
        email: String,
        password: String
    ): Response<DefaultResponse> {
        return if(email == "emailsukses@gmail.com") {
            Response.success(DataDummy.registerSuccessResponse)
        } else {
            Response.success(DataDummy.emailAlreadyRegisteredResponse)
        }
    }

    override suspend fun doLogin(email: String, password: String): Response<LoginResponse> {
        return if(email == "emailsukses@gmail.com") {
            Response.success(DataDummy.loginSuccessResponse)
        } else {
            Response.success(DataDummy.failedLoginResponse)
        }
    }

    override suspend fun postNewStory(
        Authorization: String,
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        photo: MultipartBody.Part
    ): Response<DefaultResponse> {
        val createStoryResponse = DataDummy.createStorySuccessResponse
        return Response.success(createStoryResponse)
    }
}