@file:Suppress("DEPRECATION")
package dev.studiocloud.storyapp.ui.activities.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
    private lateinit var viewModelFactory: ViewModelFactory
    private var authViewModel: AuthViewModel? = null

    private fun obtainAuthViewModel(): AuthViewModel{
        viewModelFactory = ViewModelFactory.getInstance()
        return ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        authViewModel = obtainAuthViewModel()

        if(authViewModel?.user != null){
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        with(binding){
            pbLogin.enable = false
            sbRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            pbLogin.setOnClickListener { doLogin() }
            edLoginEmail.addOnTextChange(this@LoginActivity)
            edLoginPassword.addOnTextChange(this@LoginActivity)
        }
    }

    private fun doLogin() {
        Tools().hideKeyboard(this)
        val progressDialog = ProgressDialog(this, R.style.AppCompatAlertDialogStyle)
        progressDialog.setMessage(getString(R.string.loading))
        progressDialog.show()

        authViewModel?.doLogin(
            binding.edLoginEmail.getText(),
            binding.edLoginPassword.getText(),
            onSuccess = {
                progressDialog.dismiss()
                finish()
                startActivity(Intent(this, HomeActivity::class.java))
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