package com.mvukosav.moviesapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.ActivityLoginBinding
import com.mvukosav.moviesapp.presentation.auth.login.LoginActivityViewModel
import com.mvukosav.moviesapp.ui.home.MainActivity
import com.mvukosav.moviesapp.ui.registration.RegistrationActivity
import com.mvukosav.moviesapp.ui.splash.SplashActivity
import com.mvukosav.moviesapp.utils.setGone
import com.mvukosav.moviesapp.utils.setVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickListeners()
        observeInputValidation()
        observeLogin()
        observeErrors()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        openSplashScreen()
    }

    private fun setClickListeners() {
        binding.emailInput.doOnTextChanged { text, start, before, count ->
            viewModel.validateInput(binding.passwordInput.text.toString(), text.toString())
        }
        binding.passwordInput.doOnTextChanged { text, start, before, count ->
            viewModel.validateInput(text.toString(), binding.emailInput.text.toString())
        }

        binding.registrationBtn.setOnClickListener {
            openRegistrationScreen()
        }
    }

    private fun observeInputValidation() {
        viewModel.fetchInputValidationLiveData.observe(this) {
            if (it) {
                enableLoginBtn()
                binding.errorText.setGone()
            } else {
                binding.errorText.setVisible()
            }
        }
    }

    private fun observeErrors() {
        viewModel.fetchErrorLiveData.observe(this) {
            if (it != null) {
                Log.d("Login", it)
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeLogin() {
        viewModel.fetchUserLiveData.observe(this) {
            if (it != null) {
                openMainScreen()
            } else {
                binding.loginBtn.setVisible()
                hideProgressBar()
                Log.d("Login", "Login failed")
            }
        }
    }

    private fun openMainScreen() {
        hideProgressBar()
        startActivity(MainActivity.createIntent(this))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun openSplashScreen() {
        hideProgressBar()
        startActivity(SplashActivity.createIntent(this))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun openRegistrationScreen() {
        hideProgressBar()
        startActivity(RegistrationActivity.createIntent(this))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun enableLoginBtn() {
        binding.loginBtn.apply {
            setBackgroundColor(ContextCompat.getColor(this@LoginActivity, R.color.baby_blue))
            setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.white))
            setOnClickListener {
                showProgressBar()
                binding.loginBtn.setGone()
                viewModel.loginUser(
                    binding.emailInput.text.toString(),
                    binding.passwordInput.text.toString()
                )
            }
        }
    }

    private fun hideProgressBar() {
        binding.loadingBar.setGone()
    }

    private fun showProgressBar() {
        binding.loadingBar.setVisible()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}
