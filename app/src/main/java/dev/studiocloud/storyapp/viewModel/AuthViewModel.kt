package dev.studiocloud.storyapp.viewModel

import androidx.lifecycle.ViewModel
import dev.studiocloud.storyapp.App.Companion.prefs
import dev.studiocloud.storyapp.data.repository.MainRepository
import dev.studiocloud.storyapp.data.source.network.model.LoginResult

class AuthViewModel(private val mainRepository: MainRepository?): ViewModel() {
    var user: LoginResult? = null

    init {
        user = prefs?.user
    }

    fun doRegister(
        name: String,
        email: String,
        password: String,
    ) = mainRepository?.doRegister(name, email, password)

    fun doLogin(
        email: String,
        password: String
    ) = mainRepository?.doLogin(email, password)
}