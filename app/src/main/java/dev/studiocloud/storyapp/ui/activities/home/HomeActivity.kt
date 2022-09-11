package dev.studiocloud.storyapp.ui.activities.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import dev.studiocloud.storyapp.databinding.ActivityHomeBinding
import dev.studiocloud.storyapp.viewModel.AuthViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var authViewModel: AuthViewModel? = null
    private var viewModelFactory: ViewModelFactory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory.getInstance(application)
        authViewModel = ViewModelProvider(this, viewModelFactory!!)[AuthViewModel::class.java]
    }
}