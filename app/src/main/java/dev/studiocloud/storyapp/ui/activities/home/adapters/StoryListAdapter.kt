package dev.studiocloud.storyapp.ui.activities.home.adapters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
import dev.studiocloud.storyapp.databinding.ItemStoryBinding
import eightbitlab.com.blurview.RenderScriptBlur

class StoryListAdapter(
    private var context: Context,
    private var data: MutableList<StoryItem>
): RecyclerView.Adapter<StoryListAdapter.Holder>() {
    var lastPosition = -1

    inner class Holder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder){
            with(data[position]){
                Glide.with(context)
                    .applyDefaultRequestOptions(
                        RequestOptions()
                            .override(56,56)
                            .circleCrop()
                    )
                    .load(this.photoUrl)
                    .into(binding.ivAvatar)

                Glide.with(context)
                    .applyDefaultRequestOptions(
                        RequestOptions()
                            .centerCrop()
                            .override(500,500)
                    )
                    .load(this.photoUrl)
                    .into(binding.ivStory)

                binding.viAccountContainer.setupWith(binding.root, RenderScriptBlur(context)) // or RenderEffectBlur
                    .setBlurRadius(15f)
                binding.viAccountContainer.outlineProvider = ViewOutlineProvider.BACKGROUND;
                binding.viAccountContainer.clipToOutline = true;

                binding.tvName.text = this.name
                binding.tvCaption.text = this.description
            }
        }

        animateItem(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return data.size
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