@file:Suppress("DEPRECATION")

package dev.studiocloud.storyapp.ui.fragments.home.adapters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
import dev.studiocloud.storyapp.databinding.ItemStoryBinding
import dev.studiocloud.storyapp.ui.fragments.detail.DetailActivity
import dev.studiocloud.storyapp.utils.Constant

class StoryListAdapter(
    private var context: Context,
    private var data: MutableList<StoryItem>
): RecyclerView.Adapter<StoryListAdapter.Holder>() {
    private var lastPosition = -1
    private val avatarRequestOption = RequestOptions()
        .override(56,56)
        .circleCrop()
    private val storyImageRequestOption = RequestOptions()
        .placeholder(R.drawable.placeholder)
        .override(500,500)
        .centerCrop()

    inner class Holder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder){
            with(data[position]){
                Glide.with(context)
                    .applyDefaultRequestOptions(avatarRequestOption)
                    .load(this.photoUrl)
                    .into(binding.ivUserAvatar)

                Glide.with(context)
                    .applyDefaultRequestOptions(storyImageRequestOption)
                    .load(this.photoUrl)
                    .listener(glideListener(binding))
                    .into(binding.ivItemPhoto)

                binding.tvItemName.text = this.name
                binding.tvCaption.text = this.description

                binding.ivItemPhoto.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(binding.cvContainer, context.getString(R.string.story)),
                        )

                    val intent = Intent(context,DetailActivity::class.java)
                    intent.putExtra(Constant.STORY_DATA, this)
                    context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }

        animateItem(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun glideListener(
        binding: ItemStoryBinding,
    ): RequestListener<Drawable>{
        return object : RequestListener<Drawable>{
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

    private fun animateItem(view: View, position: Int){
        val animatorSet = AnimatorSet()

        val alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0.5f, 1f)
        if (position > lastPosition){
            val translateY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 1000f, 0f)

            animatorSet.playTogether(translateY, alpha)
        } else {
            val translateY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, -1000f, 0f)

            animatorSet.playTogether(translateY, alpha)
        }

        animatorSet.start()
        lastPosition = position
    }
}