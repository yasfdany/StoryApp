package dev.studiocloud.storyapp.ui.activities.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dev.studiocloud.storyapp.App.Companion.prefs
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.databinding.ActivityHomeBinding
import dev.studiocloud.storyapp.ui.activities.home.adapters.LoadingStateAdapter
import dev.studiocloud.storyapp.ui.activities.home.adapters.StoryListAdapter
import dev.studiocloud.storyapp.ui.activities.login.LoginActivity
import dev.studiocloud.storyapp.ui.activities.maps_story.MapStoryActivity
import dev.studiocloud.storyapp.ui.activities.upload.UploadActivity
import dev.studiocloud.storyapp.viewModel.StoryViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private var storyViewModel: StoryViewModel? = null
    private var storyListAdapter: StoryListAdapter? = null
    private var doubleBackToExitPressedOnce = false
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            storyListAdapter?.refresh()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.rvStoryList.scrollToPosition(0)
            }, 1000)
        }
    }

    private fun obtainStoryViewModel(): StoryViewModel {
        viewModelFactory = ViewModelFactory.getInstance(this)
        return ViewModelProvider(this, viewModelFactory)[StoryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyViewModel = obtainStoryViewModel()
        setupStoryListView()
        getData()

        with(binding){
            ibMaps.setOnClickListener { startActivity(Intent(this@HomeActivity, MapStoryActivity::class.java)) }
            ibLogout.setOnClickListener { doLogout() }
            srStoryRefresher.setOnRefreshListener {
                storyListAdapter?.refresh()
            }

            buttonAdd.setOnClickListener {
                resultLauncher.launch(Intent(this@HomeActivity, UploadActivity::class.java))
            }

            tvInitialName.setOnClickListener {
                rvStoryList.smoothScrollToPosition(0)
            }
        }
    }

    private fun getData() {
        with(binding){
            val scaleX = ObjectAnimator.ofFloat(buttonAdd, View.SCALE_X, 0f)
            val scaleY = ObjectAnimator.ofFloat(buttonAdd, View.SCALE_Y, 0f)
            val scaleReverseX = ObjectAnimator.ofFloat(buttonAdd, View.SCALE_X, 1f)
            val scaleReverseY = ObjectAnimator.ofFloat(buttonAdd, View.SCALE_Y, 1f)
            storyListAdapter = StoryListAdapter(this@HomeActivity)

            val scaleSet = AnimatorSet()
            val scaleReverseSet = AnimatorSet()

            scaleSet.playTogether(scaleX,scaleY)
            scaleReverseSet.playTogether(scaleReverseX, scaleReverseY)

            storyListAdapter?.addLoadStateListener {
                when(it.source.refresh){
                    is LoadState.NotLoading -> {
                        srStoryRefresher.isRefreshing = false
                    }
                    LoadState.Loading -> {}
                    is LoadState.Error -> {}
                }
                when(it.source.append){
                    is LoadState.NotLoading -> {
                        scaleReverseSet.start()
                        animateLoadMoreLoading(false)
                    }
                    LoadState.Loading -> {
                        scaleSet.start()
                        animateLoadMoreLoading(true)
                    }
                    is LoadState.Error -> {}
                }
            }
            rvStoryList.adapter = storyListAdapter?.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyListAdapter?.retry()
                }
            )
            storyViewModel?.stories?.observe(this@HomeActivity) {
                storyListAdapter?.submitData(lifecycle, it)
            }
        }
    }

    private fun doLogout() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(getString(R.string.logout))
        dialog.setMessage(getString(R.string.logout_description))
        dialog.setPositiveButton(
            getString(R.string.logout)
        ) { dialogInterface, _ ->
            dialogInterface.dismiss()

            prefs?.user = null
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        dialog.setNegativeButton(
            getString(R.string.cancel)
        ) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialog.show()
    }

    private fun setupStoryListView() {
        with(binding){

            val layoutManager = LinearLayoutManager(this@HomeActivity)
            rvStoryList.layoutManager = layoutManager

            tvInitialName.text = prefs?.user?.name?.substring(0, 1)
            tvName.text = prefs?.user?.name

            viLoadingMore.translationY = 500f
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) finish()

        doubleBackToExitPressedOnce = true
        Handler(Looper.getMainLooper()).postDelayed({doubleBackToExitPressedOnce = false}, 2000)
        Toast.makeText(this, getString(R.string.exit_confirmation), Toast.LENGTH_SHORT).show()
    }

    private fun animateLoadMoreLoading(show: Boolean) {
        val animationSet = AnimatorSet()

        val translateY = ObjectAnimator.ofFloat(binding.viLoadingMore, View.TRANSLATION_Y, if(show) 0f else 500f)
        val opacity = ObjectAnimator.ofFloat(binding.viLoadingMore, View.ALPHA, if (show) 1f else 0f)

        animationSet.playTogether(translateY, opacity)
        animationSet.start()
    }
}