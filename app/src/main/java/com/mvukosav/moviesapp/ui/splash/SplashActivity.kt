package com.mvukosav.moviesapp.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mvukosav.moviesapp.databinding.ActivitySplashBinding
import com.mvukosav.moviesapp.presentation.splash.SplashViewModel
import com.mvukosav.moviesapp.utils.observeOnMainThread
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
                //odvedi me na main
                Log.d("SUCCESS", it.toString())
            }else{
                //Odvedi me na login
                Log.d("FAILED", it.toString())
            }
        }
    }

    companion object {
        private const val DELAY_MILLIS: Long = 700
        fun createIntent(context: Context) = Intent(context, SplashActivity::class.java)
    }
}