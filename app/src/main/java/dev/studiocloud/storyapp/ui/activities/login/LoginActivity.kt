package dev.studiocloud.storyapp.ui.activities.login

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dev.studiocloud.storyapp.data.repository.MainRepository
import dev.studiocloud.storyapp.databinding.ActivityLoginBinding
import dev.studiocloud.storyapp.di.Injection
import dev.studiocloud.storyapp.ui.components.OnTextChange
import dev.studiocloud.storyapp.utils.Tools
import dev.studiocloud.storyapp.viewModel.AuthViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class LoginActivity : AppCompatActivity(), OnTextChange {
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

        binding.pbLogin.enable = false
        binding.pbLogin.setOnClickListener {
        }

        binding.tfEmail.addOnTextChange(this)
        binding.tfPassword.addOnTextChange(this)
    }

    override fun onChange(text: String) {
        val isButtonEnable = Tools().isValidEmail(binding.tfEmail.getText()) && binding.tfPassword.getText().length >= 6
        binding.pbLogin.enable = isButtonEnable
    }
}