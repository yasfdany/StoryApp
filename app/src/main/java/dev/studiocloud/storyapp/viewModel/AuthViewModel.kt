package dev.studiocloud.storyapp.viewModel

import androidx.lifecycle.ViewModel
import dev.studiocloud.storyapp.App.Companion.prefs
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResult
import dev.studiocloud.storyapp.data.MainRepository

class AuthViewModel(private val mainRepository: MainRepository?): ViewModel() {
    var user: LoginResult? = null

    init {
        user = prefs?.user
    }

    fun doRegister(
        name: String,
        email: String,
        password: String,
        onSuccess: (response: DefaultResponse?) -> Unit,
        onFailed: ((message: String?) -> Unit)? = null,
    ){
        mainRepository?.doRegister(
            name,
            email,
            password,
            onSuccess = {
                onSuccess(it)
            },
            onFailed = {
                onFailed?.invoke(it)
            }
        )
    }

    fun doLogin(
        email: String,
        password: String,
        onSuccess: (response: LoginResponse?) -> Unit,
        onFailed: ((message: String?) -> Unit)? = null,
    ){
        mainRepository?.doLogin(
            email,
            password,
            onSuccess = {
                user = it?.loginResult
                prefs?.user = it?.loginResult
                onSuccess(it)
            },
            onFailed = {
                onFailed?.invoke(it)
            }
        )
    }
}