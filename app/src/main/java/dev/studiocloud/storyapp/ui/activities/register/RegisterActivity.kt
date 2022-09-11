package dev.studiocloud.storyapp.ui.activities.register

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
    private var authViewModel: AuthViewModel? = null
    private var viewModelFactory: ViewModelFactory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory.getInstance(application)
        authViewModel = if(viewModelFactory != null) ViewModelProvider(this, viewModelFactory!!)[AuthViewModel::class.java] else null

        binding.pbRegister.enable = false
        binding.ibBack.setOnClickListener { finish() }
        binding.tfName.addOnTextChange(this)
        binding.tfEmail.addOnTextChange(this)
        binding.tfPassword.addOnTextChange(this)

        binding.pbRegister.setOnClickListener {
            Tools().hideKeyboard(this)

            val progressDialog = ProgressDialog(this, R.style.AppCompatAlertDialogStyle)
            progressDialog.setMessage(getString(R.string.loading))
            progressDialog.show()

            authViewModel?.doRegister(
                binding.tfName.getText(),
                binding.tfEmail.getText(),
                binding.tfPassword.getText(),
                onRegisterSuccess = {
                    progressDialog.dismiss()
                    Toast.makeText(this,it?.message ?: "", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onRegisterFailed = {
                    progressDialog.dismiss()
                    val snackBar = Snackbar.make(binding.rootRegisterPage,it ?: "tst", Snackbar.LENGTH_SHORT)
                    snackBar.show()
                }
            )
        }
    }

    override fun onChange(text: String) {
        val isButtonEnable = binding.tfName.getText().length >= 3 && Tools().isValidEmail(binding.tfEmail.getText()) && binding.tfPassword.getText().length >= 6
        binding.pbRegister.enable = isButtonEnable
    }
}