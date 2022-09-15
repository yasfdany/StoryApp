package dev.studiocloud.storyapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.studiocloud.storyapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}