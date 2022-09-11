package dev.studiocloud.storyapp.ui.activities.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
import dev.studiocloud.storyapp.databinding.ActivityDetailBinding
import dev.studiocloud.storyapp.utils.Constant

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        val storyItem = extras?.getParcelable<StoryItem>(Constant.STORY_DATA)

        if (storyItem != null){
            Glide.with(this)
                .applyDefaultRequestOptions(
                    RequestOptions()
                        .centerCrop()
                        .override(500,500)
                )
                .load(storyItem.photoUrl)
                .into(binding.ivStory)
        }
    }
}