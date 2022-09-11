package dev.studiocloud.storyapp.ui.activities.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.data.repository.MainRepository
import dev.studiocloud.storyapp.databinding.ActivityLoginBinding
import dev.studiocloud.storyapp.di.Injection
import dev.studiocloud.storyapp.ui.activities.register.RegisterActivity
import dev.studiocloud.storyapp.ui.components.OnTextChange
import dev.studiocloud.storyapp.utils.Tools
import dev.studiocloud.storyapp.viewModel.AuthViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity(), OnTextChange {
    private lateinit var binding: ActivityLoginBinding
    private var authViewModel: AuthViewModel? = null
    private var viewModelFactory: ViewModelFactory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory.getInstance(application)
        authViewModel = if(viewModelFactory != null) ViewModelProvider(this, viewModelFactory!!)[AuthViewModel::class.java] else null

        binding.pbLogin.enable = false
        binding.sbRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.pbLogin.setOnClickListener {
            Tools().hideKeyboard(this)
            val progressDialog = ProgressDialog(this, R.style.AppCompatAlertDialogStyle)
            progressDialog.setMessage(getString(R.string.loading))
            progressDialog.show()

            authViewModel?.doLogin(
                binding.tfEmail.getText(),
                binding.tfPassword.getText(),
                onLoginSuccess = {
                    progressDialog.dismiss()
                },
                onLoginFailed = {
                    progressDialog.dismiss()
                    val snackBar = Snackbar.make(binding.rootLoginPage,it ?: "", Snackbar.LENGTH_SHORT)
                    snackBar.show()
                }
            )
        }

        binding.tfEmail.addOnTextChange(this)
        binding.tfPassword.addOnTextChange(this)
    }

    override fun onChange(text: String) {
        val isButtonEnable = Tools().isValidEmail(binding.tfEmail.getText()) && binding.tfPassword.getText().length >= 6
        binding.pbLogin.enable = isButtonEnable
    }
}