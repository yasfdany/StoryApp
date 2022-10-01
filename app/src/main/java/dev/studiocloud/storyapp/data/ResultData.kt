package dev.studiocloud.storyapp.data

sealed class ResultData<out R> private constructor() {
    data class Success<out T>(val data: T) : ResultData<T>()
    data class Error(val error: String) : ResultData<Nothing>()
    object Loading : ResultData<Nothing>()
}