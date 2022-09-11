package dev.studiocloud.storyapp.ui.activities.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationSet
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.studiocloud.storyapp.App.Companion.prefs
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
import dev.studiocloud.storyapp.databinding.ActivityHomeBinding
import dev.studiocloud.storyapp.ui.activities.home.adapters.StoryListAdapter
import dev.studiocloud.storyapp.viewModel.StoryViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var viewModelFactory: ViewModelFactory? = null
    private var storyViewModel: StoryViewModel? = null
    private var storyListAdapter: StoryListAdapter? = null
    var lastSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory.getInstance(application)
        storyViewModel = ViewModelProvider(this, viewModelFactory!!)[StoryViewModel::class.java]
        storyViewModel?.getStory(true)

        storyListAdapter = StoryListAdapter(this, storyViewModel?.stories?.value ?: mutableListOf());
        binding.rvStoryList.adapter = storyListAdapter

        val linearLayout = LinearLayoutManager(this)

        binding.tvInitial.text = prefs?.user?.name?.substring(0,1)
        binding.tvName.text = prefs?.user?.name

        binding.viProgressLoadMore.translationY = 500f;

        binding.rvStoryList.layoutManager = linearLayout
        binding.rvStoryList.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (linearLayout.findLastCompletelyVisibleItemPosition() == (storyViewModel?.stories?.value?.size ?: 0) - 1){
                    animateLoadMoreLoading(true)

                    storyViewModel?.getStory(onFinish = {
                        Handler().postDelayed({
                            animateLoadMoreLoading(false)
                        }, 500)
                    })
                }
            }
        })


        storyViewModel?.stories?.observe(this){
            storyListAdapter?.notifyItemRangeInserted(lastSize, it.size);

            lastSize = it.size
        }
    }

    private fun animateLoadMoreLoading(show: Boolean) {
        val animationSet = AnimatorSet()

        val translateY = ObjectAnimator.ofFloat(binding.viProgressLoadMore, View.TRANSLATION_Y, if(show) 0f else 500f)
        val opacity = ObjectAnimator.ofFloat(binding.viProgressLoadMore, View.ALPHA, if (show) 1f else 0f)

        animationSet.playTogether(translateY, opacity)
        animationSet.start()
    }
}