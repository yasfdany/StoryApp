package dev.studiocloud.storyapp.ui.activities.home.adapters

import androidx.recyclerview.widget.DiffUtil
import dev.studiocloud.storyapp.data.source.network.model.StoryItem

class StoryDiffCallback(
    private val oldStory: MutableList<StoryItem>,
    private val newStory: List<StoryItem>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldStory.size

    override fun getNewListSize(): Int = newStory.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStory[oldItemPosition].id == newStory[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStory[oldItemPosition].id == newStory[newItemPosition].id
    }
}