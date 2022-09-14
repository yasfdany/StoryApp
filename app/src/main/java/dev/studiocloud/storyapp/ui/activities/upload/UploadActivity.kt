package dev.studiocloud.storyapp.ui.activities.upload

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.studiocloud.storyapp.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}