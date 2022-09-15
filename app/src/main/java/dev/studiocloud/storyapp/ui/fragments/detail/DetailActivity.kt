package dev.studiocloud.storyapp.ui.fragments.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
import dev.studiocloud.storyapp.databinding.ActivityDetailBinding
import dev.studiocloud.storyapp.utils.Constant

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val avatarRequestOption = RequestOptions()
        .override(56,56)
        .circleCrop()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        val storyItem = extras?.getParcelable<StoryItem>(Constant.STORY_DATA)

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        if (storyItem != null){
            binding.tvDetailName.text = storyItem.name
            binding.tvDetailDescription.text = storyItem.description

            Glide.with(this)
                .applyDefaultRequestOptions(avatarRequestOption)
                .load(storyItem.photoUrl)
                .into(binding.ivUserAvatar)

            Glide.with(this)
                .applyDefaultRequestOptions(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .centerCrop()
                        .override(500,500)
                )
                .load(storyItem.photoUrl)
                .listener(glideListener())
                .into(binding.ivDetailPhoto)
        }
    }

    private fun glideListener(
    ): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                binding.pbLoadingImage.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                binding.pbLoadingImage.visibility = View.GONE
                return false
            }
        }
    }
}