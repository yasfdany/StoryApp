package dev.studiocloud.storyapp.data.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.studiocloud.storyapp.data.services.ApiClient
import dev.studiocloud.storyapp.data.services.ApiService
import dev.studiocloud.storyapp.data.services.responses.DefaultResponse
import dev.studiocloud.storyapp.data.services.responses.LoginResponse
import dev.studiocloud.storyapp.data.services.responses.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel: ViewModel() {
    private val client: ApiService? = ApiClient().get()
    private val user: MutableLiveData<LoginResult?> = MutableLiveData()

    fun doRegister(
        name: String,
        email: String,
        password: String,
        onRegisterSuccess: () -> Unit,
        onRegisterFailed: ((message: String?) -> Unit)? = null,
    ){
        client?.doRegister(name ,email, password)?.enqueue(object : Callback<DefaultResponse?>{
            override fun onResponse(
                call: Call<DefaultResponse?>,
                response: Response<DefaultResponse?>
            ) {
                if (response.code() == 200) {
                    onRegisterSuccess()
                } else {
                    onRegisterFailed?.invoke(response.body()?.message)
                }
            }

            override fun onFailure(call: Call<DefaultResponse?>, t: Throwable) {
                onRegisterFailed?.invoke(t.message)
            }
        })
    }

    fun doLogin(
        email: String,
        password: String,
        onLoginSuccess: () -> Unit,
        onLoginFailed: ((message: String?) -> Unit)? = null,
    ){
        client?.doLogin(email, password)?.enqueue(object : Callback<LoginResponse?>{
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.code() == 200) {
                    user.value = response.body()?.loginResult;
                    onLoginSuccess()
                } else {
                    onLoginFailed?.invoke(response.body()?.message)
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                onLoginFailed?.invoke(t.message)
            }
        })
    }
}