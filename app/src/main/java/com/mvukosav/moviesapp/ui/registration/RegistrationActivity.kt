package com.mvukosav.moviesapp.ui.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.ActivityRegistrationBinding
import com.mvukosav.moviesapp.presentation.auth.registration.RegistrationActivityViewModel
import com.mvukosav.moviesapp.ui.home.MainActivity
import com.mvukosav.moviesapp.ui.login.LoginActivity
import com.mvukosav.moviesapp.ui.splash.SplashActivity
import com.mvukosav.moviesapp.utils.setGone
import com.mvukosav.moviesapp.utils.setVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val viewModel: RegistrationActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListeners()
        observe()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        openSplashScreen()
    }

    private fun setOnClickListeners() {
        binding.loginBtn.setOnClickListener {
            openLoginScreen()
        }

        binding.confirmPasswordInput.doAfterTextChanged {
            viewModel.validateInputFields(
                binding.fullNameInput.text.toString(),
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString(),
                it.toString()
            )
        }
    }

    private fun observe() {
        observeInputValidation()
        observeUser()
        observeErrors()
    }

    private fun observeInputValidation() {
        viewModel.fetchInputValidationLiveData.observe(this) {
            if (it) {
                enableRegistrationBtn()
                binding.errorText.setGone()
            } else {
                binding.errorText.setVisible()
            }
        }
    }

    private fun observeUser() {
        viewModel.fetchUserLiveData.observe(this) {
            if (it != null) {
                openMainScreen()
            } else {
                binding.loginBtn.setVisible()
                hideProgressBar()
                Log.d("Registration", "Registration failed")
            }
        }
    }

    private fun observeErrors() {
        viewModel.fetchErrorLiveData.observe(this) {
            if (it != null) {
                Log.d("Registration", it)
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enableRegistrationBtn() {
        binding.createNewAccountBtn.apply {
            setBackgroundColor(ContextCompat.getColor(this@RegistrationActivity, R.color.baby_blue))
            setTextColor(ContextCompat.getColor(this@RegistrationActivity, R.color.white))
            setOnClickListener {
                showProgressBar()
                binding.loginBtn.setGone()
                viewModel.createUser(
                    binding.fullNameInput.text.toString(),
                    binding.emailInput.text.toString(),
                    binding.passwordInput.text.toString()
                )
            }
        }
    }

    private fun openMainScreen() {
        hideProgressBar()
        startActivity(MainActivity.createIntent(this))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun openLoginScreen() {
        hideProgressBar()
        startActivity(LoginActivity.createIntent(this))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun openSplashScreen() {
        hideProgressBar()
        startActivity(SplashActivity.createIntent(this))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun hideProgressBar() {
        binding.loadingBar.setGone()
    }

    private fun showProgressBar() {
        binding.loadingBar.setVisible()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, RegistrationActivity::class.java)
    }
}