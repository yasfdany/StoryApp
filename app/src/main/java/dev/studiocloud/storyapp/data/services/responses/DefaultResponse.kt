package dev.studiocloud.storyapp.data.services.responses

import com.google.gson.annotations.SerializedName

data class DefaultResponse(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
