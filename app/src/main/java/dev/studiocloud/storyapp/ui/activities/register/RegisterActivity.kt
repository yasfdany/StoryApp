@file:Suppress("DEPRECATION")

package dev.studiocloud.storyapp.ui.activities.register

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.databinding.ActivityRegisterBinding
import dev.studiocloud.storyapp.ui.components.OnTextChange
import dev.studiocloud.storyapp.utils.Tools
import dev.studiocloud.storyapp.viewModel.AuthViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class RegisterActivity : AppCompatActivity(), OnTextChange {
    private lateinit var binding: ActivityRegisterBinding
    private var viewModelFactory: ViewModelFactory? = null
    private var authViewModel: AuthViewModel? = null

    private fun obtainAuthViewModel(): AuthViewModel{
        viewModelFactory = ViewModelFactory.getInstance()
        return ViewModelProvider(this, viewModelFactory!!)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = obtainAuthViewModel()

        binding.pbRegister.enable = false
        binding.ibBack.setOnClickListener { finish() }
        binding.edRegisterName.addOnTextChange(this)
        binding.edRegisterEmail.addOnTextChange(this)
        binding.edRegisterPassword.addOnTextChange(this)

        binding.pbRegister.setOnClickListener { doRegister() }
    }

    private fun doRegister() {
        Tools().hideKeyboard(this)

        val progressDialog = ProgressDialog(this, R.style.AppCompatAlertDialogStyle)
        progressDialog.setMessage(getString(R.string.loading))
        progressDialog.show()

        authViewModel?.doRegister(
            binding.edRegisterName.getText(),
            binding.edRegisterEmail.getText(),
            binding.edRegisterPassword.getText(),
            onSuccess = {
                progressDialog.dismiss()
                Toast.makeText(this, it?.message ?: "", Toast.LENGTH_SHORT).show()
                finish()
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