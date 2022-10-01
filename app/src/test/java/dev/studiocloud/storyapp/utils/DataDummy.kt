package dev.studiocloud.storyapp.utils

import com.google.gson.Gson
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.StoryResponse

object DataDummy {
    val loginSuccessResponse: LoginResponse = Gson().fromJson(
        "{\"error\":false,\"message\":\"success\",\"loginResult\":{\"userId\":\"user-RBCBHlZuTeQFWnxK\",\"name\":\"Test Case\",\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVJCQ0JIbFp1VGVRRldueEsiLCJpYXQiOjE2NjQ2MTQyMzh9.tUL-I9UEAjtbXb2e-kOvBTONhY0HDBLjZqZjUTg3qt0\"}}",
        LoginResponse::class.java,
    )
    val failedLoginResponse: DefaultResponse = Gson().fromJson(
        "{\"error\":true,\"message\":\"Invalid password\"}",
        DefaultResponse::class.java
    )
    val registerSuccessResponse: DefaultResponse = Gson().fromJson(
        "{\"error\": false,\"message\": \"User Created\"}",
        DefaultResponse::class.java
    )
    val emailAlreadyRegisteredResponse: DefaultResponse = Gson().fromJson(
        "{\"error\":true,\"message\":\"Email is already taken\"}",
        DefaultResponse::class.java
    )
    val getStoryWithLocationResponse: StoryResponse = Gson().fromJson(
        "{\"error\":false,\"message\":\"Stories fetched successfully\",\"listStory\":[{\"id\":\"story-47v_BBjpofODtin5\",\"name\":\"ismar\",\"description\":\"edd\",\"photoUrl\":\"https://story-api.dicoding.dev/images/stories/photos-1664636030182_u5DJnrHP.jpg\",\"createdAt\":\"2022-10-01T14:53:50.185Z\",\"lat\":-7.6517809,\"lon\":111.0476794},{\"id\":\"story-wnraQWdOcA2ZZnC4\",\"name\":\"Tester\",\"description\":\"Testing Images of Dicoding API with Weather APPS\",\"photoUrl\":\"https://story-api.dicoding.dev/images/stories/photos-1664634517481_HDbdi2Jc.png\",\"createdAt\":\"2022-10-01T14:28:37.483Z\",\"lat\":37.419857,\"lon\":-122.078827},{\"id\":\"story-Hc5sjT-9rGgj6rIh\",\"name\":\"Tester\",\"description\":\"Testing Images of Dicoding API with Weather APPS\",\"photoUrl\":\"https://story-api.dicoding.dev/images/stories/photos-1664633193740_Az6-1Bmf.png\",\"createdAt\":\"2022-10-01T14:06:33.741Z\",\"lat\":37.419857,\"lon\":-122.078827},{\"id\":\"story-3eUvZ7jQxk-_UGd-\",\"name\":\"Tester\",\"description\":\"Testing Images of Dicoding API with Weather APPS\",\"photoUrl\":\"https://story-api.dicoding.dev/images/stories/photos-1664632612714_UnKL1Evd.png\",\"createdAt\":\"2022-10-01T13:56:52.716Z\",\"lat\":37.419857,\"lon\":-122.078827},{\"id\":\"story-mjXsMbokdA8YwzMR\",\"name\":\"i\",\"description\":\"W\",\"photoUrl\":\"https://story-api.dicoding.dev/images/stories/photos-1664628124575_xmHQsosl.jpg\",\"createdAt\":\"2022-10-01T12:42:04.577Z\",\"lat\":-6.8963845,\"lon\":107.5958853},{\"id\":\"story-CFPrRRGLGFMvHVP2\",\"name\":\"jr\",\"description\":\"tes\",\"photoUrl\":\"https://story-api.dicoding.dev/images/stories/photos-1664623986129_HpYI0DZe.jpg\",\"createdAt\":\"2022-10-01T11:33:06.131Z\",\"lat\":37.422092,\"lon\":-122.08392},{\"id\":\"story-k9hi-WA7HCPdMiUu\",\"name\":\"tri\",\"description\":\"okky boy\",\"photoUrl\":\"https://story-api.dicoding.dev/images/stories/photos-1664615087142_m3gc6pE4.jpg\",\"createdAt\":\"2022-10-01T09:04:47.143Z\",\"lat\":38.889248,\"lon\":-77.050636},{\"id\":\"story-ZbCp7Y4R4rSx6oCp\",\"name\":\"Ya'suf Dany\",\"description\":\"Halo kucing\",\"photoUrl\":\"https://story-api.dicoding.dev/images/stories/photos-1664612590559_j0PNKN9N.jpg\",\"createdAt\":\"2022-10-01T08:23:10.560Z\",\"lat\":37.4220936,\"lon\":37.4220936},{\"id\":\"story-MHPP5obbidAbDqDm\",\"name\":\"Ya'suf Dany\",\"description\":\"Halo Kucing\",\"photoUrl\":\"https://story-api.dicoding.dev/images/stories/photos-1664609436215_FpkMpGNx.jpg\",\"createdAt\":\"2022-10-01T07:30:36.216Z\",\"lat\":37.4220936,\"lon\":37.4220936},{\"id\":\"story-IIONvnf7oaU3x1rS\",\"name\":\"kakang\",\"description\":\"macet\\n\",\"photoUrl\":\"https://story-api.dicoding.dev/images/stories/photos-1664608567812_dL6Ov2lJ.jpg\",\"createdAt\":\"2022-10-01T07:16:07.815Z\",\"lat\":-6.9033961,\"lon\":106.8610524}]}",
        StoryResponse::class.java
    )
    val getEmptyStoryResponse: StoryResponse = Gson().fromJson(
        "{\"error\":false,\"message\":\"Stories fetched successfully\",\"listStory\":[]}",
        StoryResponse::class.java
    )
}