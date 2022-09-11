package dev.studiocloud.storyapp.ui.activities.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
import dev.studiocloud.storyapp.databinding.ItemStoryBinding

class StoryListAdapter(
    private var context: Context,
    private var data: MutableList<StoryItem>
): RecyclerView.Adapter<StoryListAdapter.Holder>() {
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
                            .override(500,500)
                            .centerCrop()
                    )
                    .load(this.photoUrl)
                    .into(binding.ivStory)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}