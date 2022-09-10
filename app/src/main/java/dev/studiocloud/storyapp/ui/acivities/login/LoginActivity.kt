package dev.studiocloud.storyapp.ui.acivities.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dev.studiocloud.storyapp.data.repository.MainRepository
import dev.studiocloud.storyapp.databinding.ActivityLoginBinding
import dev.studiocloud.storyapp.di.Injection
import dev.studiocloud.storyapp.viewModel.AuthViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var authViewModel: AuthViewModel? = null
    private var mainRepository: MainRepository? = null
    private var viewModelFactory: ViewModelFactory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainRepository = Injection.provideRepository(application)
        viewModelFactory = ViewModelFactory.getInstance(mainRepository)
        authViewModel = if(viewModelFactory != null) ViewModelProvider(this, viewModelFactory!!)[AuthViewModel::class.java] else null
    }
}