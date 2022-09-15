@file:Suppress("DEPRECATION")

package dev.studiocloud.storyapp.ui.fragments.register

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.databinding.FragmentRegisterBinding
import dev.studiocloud.storyapp.di.Injection
import dev.studiocloud.storyapp.ui.components.OnTextChange
import dev.studiocloud.storyapp.utils.Tools
import dev.studiocloud.storyapp.viewModel.AuthViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class RegisterFragment : Fragment(), OnTextChange {
    private lateinit var binding: FragmentRegisterBinding
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
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        navController = findNavController()

        authViewModel = obtainAuthViewModel()

        binding.pbRegister.enable = false
        binding.ibBack.setOnClickListener {
            navController.popBackStack()
        }
        binding.edRegisterName.addOnTextChange(this)
        binding.edRegisterEmail.addOnTextChange(this)
        binding.edRegisterPassword.addOnTextChange(this)

        binding.pbRegister.setOnClickListener { doRegister() }

        return binding.root
    }

    private fun doRegister() {
        Tools().hideKeyboard(activity as Activity)

        val progressDialog = ProgressDialog(activity, R.style.AppCompatAlertDialogStyle)
        progressDialog.setMessage(getString(R.string.loading))
        progressDialog.show()

        authViewModel?.doRegister(
            binding.edRegisterName.getText(),
            binding.edRegisterEmail.getText(),
            binding.edRegisterPassword.getText(),
            onSuccess = {
                progressDialog.dismiss()
                Toast.makeText(activity, it?.message ?: "", Toast.LENGTH_SHORT).show()
                /*finish()*/
            },
            onFailed = {
                progressDialog.dismiss()
                val snackBar =
                    Snackbar.make(binding.clRegisterPage, it ?: "tst", Snackbar.LENGTH_SHORT)
                snackBar.show()
            }
        )
    }

    override fun onChange(text: String) {
        val isButtonEnable = binding.edRegisterName.getText().length >= 3 &&
                Tools().isValidEmail(binding.edRegisterEmail.getText()) &&
                binding.edRegisterPassword.getText().length >= 6
        binding.pbRegister.enable = isButtonEnable
    }
}