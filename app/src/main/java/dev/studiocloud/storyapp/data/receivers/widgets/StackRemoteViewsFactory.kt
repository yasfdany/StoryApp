package dev.studiocloud.storyapp.data.receivers.widgets

import android.content.Context
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
import dev.studiocloud.storyapp.utils.Prefs

internal class StackRemoteViewsFactory(private val mContext: Context): RemoteViewsService.RemoteViewsFactory {
    private val data: MutableList<StoryItem> = mutableListOf()
    private lateinit var prefs: Prefs

    override fun onCreate() {
        prefs = Prefs(mContext)
    }

    override fun onDataSetChanged() {
        data.clear()
        data.addAll(prefs.stories)
    }

    override fun onDestroy() {
    }

    override fun getCount() = data.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.story_stack_item)

        try {
            val bitmap: Bitmap = Glide.with(mContext)
                .asBitmap()
                .load(data[position].photoUrl)
                .submit(500, 500)
                .get()
            rv.setImageViewBitmap(R.id.iv_story_stack, bitmap)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}