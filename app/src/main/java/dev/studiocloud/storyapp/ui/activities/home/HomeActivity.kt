package dev.studiocloud.storyapp.ui.activities.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.studiocloud.storyapp.databinding.ActivityHomeBinding
import dev.studiocloud.storyapp.ui.activities.home.adapters.StoryListAdapter
import dev.studiocloud.storyapp.viewModel.StoryViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var viewModelFactory: ViewModelFactory? = null
    private var storyViewModel: StoryViewModel? = null
    private var storyListAdapter: StoryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory.getInstance(application)
        storyViewModel = ViewModelProvider(this, viewModelFactory!!)[StoryViewModel::class.java]
        storyViewModel?.getStory(true)

        binding.rvStoryList.layoutManager = LinearLayoutManager(this)

        storyViewModel?.stories?.observe(this){
            Log.d("hehe", it?.size.toString())
            storyListAdapter = StoryListAdapter(this, it);
            binding.rvStoryList.adapter = storyListAdapter
        }
    }
}