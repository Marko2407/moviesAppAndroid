package com.mvukosav.moviesapp.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.ActivitySplashBinding
import com.mvukosav.moviesapp.presentation.splash.SplashViewModel
import com.mvukosav.moviesapp.ui.home.MainActivity
import com.mvukosav.moviesapp.ui.login.LoginActivity
import com.mvukosav.moviesapp.ui.splash.SplashActivity.Companion.createIntent
import com.mvukosav.moviesapp.utils.observeOnMainThread
import com.mvukosav.moviesapp.utils.setGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launchWhenCreated {
            delay(DELAY_MILLIS)
            viewModel.userLogin()
        }

        observeOnMainThread(viewModel.fetchUserLiveData){
            if (it != null){
                openMainScreen()
                Log.d("SUCCESS", it.toString())
            }else{
                openLoginScreen()
                Log.d("FAILED", it.toString())
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

    private fun hideProgressBar(){
        binding.loadingBar.setGone()
    }

    companion object {
        private const val DELAY_MILLIS: Long = 700
        fun createIntent(context: Context) = Intent(context, SplashActivity::class.java)
    }
}