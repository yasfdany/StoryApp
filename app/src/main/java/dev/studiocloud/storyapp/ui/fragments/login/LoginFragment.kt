@file:Suppress("DEPRECATION")
package dev.studiocloud.storyapp.ui.fragments.login

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.databinding.FragmentLoginBinding
import dev.studiocloud.storyapp.di.Injection
import dev.studiocloud.storyapp.ui.components.OnTextChange
import dev.studiocloud.storyapp.utils.Tools
import dev.studiocloud.storyapp.viewModel.AuthViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class LoginFragment : Fragment(), OnTextChange {
    private lateinit var binding: FragmentLoginBinding
    private var viewModelFactory: ViewModelFactory? = null
    private var authViewModel: AuthViewModel? = null
    private lateinit var navController: NavController

    private fun obtainAuthViewModel(): AuthViewModel{
        viewModelFactory = Injection.provideViewModelFactory()
        return ViewModelProvider(requireActivity(), viewModelFactory!!)[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        navController = findNavController()

        authViewModel = obtainAuthViewModel()

        binding.pbLogin.enable = false
        binding.sbRegister.setOnClickListener {
            navController.navigate(R.id.navigateLoginToRegister)
        }
        binding.pbLogin.setOnClickListener { doLogin() }
        binding.edLoginEmail.addOnTextChange(this)
        binding.edLoginPassword.addOnTextChange(this)

        return binding.root
    }

    private fun doLogin() {
        Tools().hideKeyboard(activity as Activity)
        val progressDialog = ProgressDialog(activity, R.style.AppCompatAlertDialogStyle)
        progressDialog.setMessage(getString(R.string.loading))
        progressDialog.show()

        authViewModel?.doLogin(
            binding.edLoginEmail.getText(),
            binding.edLoginPassword.getText(),
            onSuccess = {
                progressDialog.dismiss()
                navController.navigate(R.id.navigateLoginToHome)
            },
            onFailed = {
                progressDialog.dismiss()
                val snackBar = Snackbar.make(binding.clLoginPage, it ?: "", Snackbar.LENGTH_SHORT)
                snackBar.show()
            }
        )
    }

    override fun onChange(text: String) {
        val isButtonEnable = Tools().isValidEmail(binding.edLoginEmail.getText()) && binding.edLoginPassword.getText().length >= 6
        binding.pbLogin.enable = isButtonEnable
    }
}