@file:Suppress("DEPRECATION")
package dev.studiocloud.storyapp.ui.activities.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dev.studiocloud.storyapp.R
import dev.studiocloud.storyapp.databinding.ActivityLoginBinding
import dev.studiocloud.storyapp.ui.activities.home.HomeActivity
import dev.studiocloud.storyapp.ui.activities.register.RegisterActivity
import dev.studiocloud.storyapp.ui.components.OnTextChange
import dev.studiocloud.storyapp.utils.Tools
import dev.studiocloud.storyapp.viewModel.AuthViewModel
import dev.studiocloud.storyapp.viewModel.ViewModelFactory

class LoginActivity : AppCompatActivity(), OnTextChange {
    private lateinit var binding: ActivityLoginBinding
    private var viewModelFactory: ViewModelFactory? = null
    private var authViewModel: AuthViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory.getInstance()
        authViewModel = ViewModelProvider(this, viewModelFactory!!)[AuthViewModel::class.java]

        if(authViewModel?.user != null){
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }

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
                onSuccess = {
                    progressDialog.dismiss()
                    finish()
                    startActivity(Intent(this, HomeActivity::class.java))
                },
                onFailed = {
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