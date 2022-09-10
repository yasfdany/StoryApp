package dev.studiocloud.storyapp.data

import androidx.lifecycle.LiveData
import dev.studiocloud.storyapp.data.model.DefaultResponse
import dev.studiocloud.storyapp.data.model.LoginResponse

interface MainDataSource {
    fun doLogin(
        email: String,
        password: String,
        onLoginSuccess: (response: LoginResponse?) -> Unit,
        onLoginFailed: ((message: String?) -> Unit)? = null,
    ): LiveData<LoginResponse?>

    fun doRegister(
        name: String,
        email: String,
        password: String,
        onRegisterSuccess: (response: DefaultResponse?) -> Unit,
        onRegisterFailed: ((message: String?) -> Unit)? = null,
    ): LiveData<DefaultResponse?>
}