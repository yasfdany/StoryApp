package dev.studiocloud.storyapp.ui.fragments.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.studiocloud.storyapp.App.Companion.prefs
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.databinding.FragmentHomeBinding
import dev.studiocloud.storyapp.di.Injection
import dev.studiocloud.storyapp.ui.fragments.home.adapters.StoryListAdapter
import dev.studiocloud.storyapp.viewModel.StoryViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private var viewModelFactory: ViewModelFactory? = null
    private var storyViewModel: StoryViewModel? = null
    private var storyListAdapter: StoryListAdapter? = null
    private var doubleBackToExitPressedOnce = false
    private var lastSize = 0

    private fun obtainStoryViewModel(): StoryViewModel {
        viewModelFactory = Injection.provideViewModelFactory()
        return ViewModelProvider(this, viewModelFactory!!)[StoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        navController = findNavController()

        storyViewModel = obtainStoryViewModel()
        storyViewModel?.getStory(true)
        setupStoryListView()

        binding.ibLogout.setOnClickListener { doLogout() }

        storyViewModel?.stories?.observe(requireActivity()){
            if (lastSize < it.size && lastSize != it.size){
                storyListAdapter?.notifyItemRangeInserted(lastSize, it.size)
            } else if(lastSize > it.size && lastSize != it.size) {
                storyListAdapter?.notifyItemRangeRemoved(0, lastSize)
            }
            lastSize = it.size
        }

        binding.srStoryRefresher.setOnRefreshListener {
            storyViewModel?.getStory(true, onFinish = {
                binding.srStoryRefresher.isRefreshing = false
            })
        }

        binding.buttonAdd.setOnClickListener {
        }

        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) requireActivity().finish()

                doubleBackToExitPressedOnce = true
                Handler(Looper.getMainLooper()).postDelayed({doubleBackToExitPressedOnce = false}, 2000)
                Toast.makeText(activity, getString(R.string.exit_confirmation), Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    private fun doLogout() {
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle(getString(R.string.logout))
        dialog.setMessage(getString(R.string.logout_description))
        dialog.setPositiveButton(
            getString(R.string.logout)
        ) { dialogInterface, _ ->
            dialogInterface.dismiss()

            prefs?.user = null
            navController.navigate(R.id.navigateLogout)
        }
        dialog.setNegativeButton(
            getString(R.string.cancel)
        ) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialog.show()
    }

    private fun setupStoryListView() {
        val linearLayout = LinearLayoutManager(activity)
        val scaleX = ObjectAnimator.ofFloat(binding.buttonAdd, View.SCALE_X, 0f)
        val scaleY = ObjectAnimator.ofFloat(binding.buttonAdd, View.SCALE_Y, 0f)
        val scaleReverseX = ObjectAnimator.ofFloat(binding.buttonAdd, View.SCALE_X, 1f)
        val scaleReverseY = ObjectAnimator.ofFloat(binding.buttonAdd, View.SCALE_Y, 1f)

        val scaleSet = AnimatorSet()
        val scaleReverseSet = AnimatorSet()

        scaleSet.playTogether(scaleX,scaleY)
        scaleReverseSet.playTogether(scaleReverseX, scaleReverseY)

        storyListAdapter = StoryListAdapter(requireActivity(), storyViewModel?.stories?.value ?: mutableListOf())
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

    private fun animateLoadMoreLoading(show: Boolean) {
        val animationSet = AnimatorSet()

        val translateY = ObjectAnimator.ofFloat(binding.viLoadingMore, View.TRANSLATION_Y, if(show) 0f else 500f)
        val opacity = ObjectAnimator.ofFloat(binding.viLoadingMore, View.ALPHA, if (show) 1f else 0f)

        animationSet.playTogether(translateY, opacity)
        animationSet.start()
    }
}