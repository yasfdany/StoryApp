package dev.studiocloud.storyapp.ui.activities.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.studiocloud.storyapp.App.Companion.prefs
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.databinding.ActivityHomeBinding
import dev.studiocloud.storyapp.ui.activities.home.adapters.StoryListAdapter
import dev.studiocloud.storyapp.ui.activities.login.LoginActivity
import dev.studiocloud.storyapp.ui.activities.upload.UploadActivity
import dev.studiocloud.storyapp.viewModel.StoryViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var viewModelFactory: ViewModelFactory? = null
    private var storyViewModel: StoryViewModel? = null
    private var storyListAdapter: StoryListAdapter? = null
    private var doubleBackToExitPressedOnce = false
    private var lastSize = 0
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            storyViewModel?.getStory(true)
        }
    }
    private fun obtainStoryViewModel(): StoryViewModel {
        viewModelFactory = ViewModelFactory.getInstance()
        return ViewModelProvider(this, viewModelFactory!!)[StoryViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyViewModel = obtainStoryViewModel()
        storyViewModel?.getStory(true)
        setupStoryListView()

        binding.ibLogout.setOnClickListener { doLogout() }

        storyViewModel?.stories?.observe(this){
            if (lastSize < it.size){
                storyListAdapter?.notifyItemRangeInserted(lastSize, it.size)
            } else {
                storyListAdapter?.notifyDataSetChanged()
            }
            lastSize = it.size
        }

        binding.srStoryRefresher.setOnRefreshListener {
            storyViewModel?.getStory(true, onFinish = {
                binding.srStoryRefresher.isRefreshing = false
            })
        }

        binding.buttonAdd.setOnClickListener {
            resultLauncher.launch(Intent(this, UploadActivity::class.java))
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
        val linearLayout = LinearLayoutManager(this)
        val scaleX = ObjectAnimator.ofFloat(binding.buttonAdd, View.SCALE_X, 0f)
        val scaleY = ObjectAnimator.ofFloat(binding.buttonAdd, View.SCALE_Y, 0f)
        val scaleReverseX = ObjectAnimator.ofFloat(binding.buttonAdd, View.SCALE_X, 1f)
        val scaleReverseY = ObjectAnimator.ofFloat(binding.buttonAdd, View.SCALE_Y, 1f)

        val scaleSet = AnimatorSet()
        val scaleReverseSet = AnimatorSet()

        scaleSet.playTogether(scaleX,scaleY)
        scaleReverseSet.playTogether(scaleReverseX, scaleReverseY)

        storyListAdapter = StoryListAdapter(this, storyViewModel?.stories?.value ?: mutableListOf())
        binding.rvStoryList.adapter = storyListAdapter

        binding.tvInitialName.text = prefs?.user?.name?.substring(0, 1)
        binding.tvName.text = prefs?.user?.name

        binding.viLoadingMore.translationY = 500f

        binding.rvStoryList.layoutManager = linearLayout
        binding.rvStoryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (linearLayout.findLastCompletelyVisibleItemPosition() == (storyViewModel?.stories?.value?.size
                        ?: 0) - 1
                ) {
                    scaleSet.start()
                    animateLoadMoreLoading(true)

                    storyViewModel?.getStory(onFinish = {
                        Handler(Looper.getMainLooper()).postDelayed({
                            scaleReverseSet.start()
                            animateLoadMoreLoading(false)
                        }, 500)
                    })
                }
            }
        })
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