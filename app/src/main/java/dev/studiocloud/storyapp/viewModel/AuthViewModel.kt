package dev.studiocloud.storyapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.studiocloud.storyapp.App.Companion.prefs
import dev.studiocloud.storyapp.data.model.DefaultResponse
import dev.studiocloud.storyapp.data.model.LoginResponse
import dev.studiocloud.storyapp.data.model.LoginResult
import dev.studiocloud.storyapp.data.repository.MainRepository

class AuthViewModel(private val mainRepository: MainRepository?): ViewModel() {
    var user: LoginResult? = null

    init {
        user = prefs?.user
    }

    fun doRegister(
        name: String,
        email: String,
        password: String,
        onRegisterSuccess: (response: DefaultResponse?) -> Unit,
        onRegisterFailed: ((message: String?) -> Unit)? = null,
    ){
        mainRepository?.doRegister(
            name,
            email,
            password,
            onRegisterSuccess = {
                onRegisterSuccess(it)
            },
            onRegisterFailed = {
                onRegisterFailed?.invoke(it)
            }
        )
    }

    fun doLogin(
        email: String,
        password: String,
        onLoginSuccess: (response: LoginResponse?) -> Unit,
        onLoginFailed: ((message: String?) -> Unit)? = null,
    ){
        mainRepository?.doLogin(
            email,
            password,
            onLoginSuccess = {
                user = it?.loginResult;
                prefs?.user = it?.loginResult
                onLoginSuccess(it)
            },
            onLoginFailed = {
                onLoginFailed?.invoke(it)
            }
        )
    }
}