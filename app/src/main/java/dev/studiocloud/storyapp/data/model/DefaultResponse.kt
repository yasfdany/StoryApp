package dev.studiocloud.storyapp.data.model

import com.google.gson.annotations.SerializedName

data class DefaultResponse(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)