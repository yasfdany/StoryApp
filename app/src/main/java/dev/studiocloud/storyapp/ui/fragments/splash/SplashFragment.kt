package dev.studiocloud.storyapp.ui.fragments.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dev.studiocloud.storyapp.App
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        navController = findNavController()

        if(App.prefs?.user != null){
            navController.navigate(R.id.navigateSplashToHome)
        } else {
            navController.navigate(R.id.navigateSplashToLogin)
        }

        return binding.root
    }
}