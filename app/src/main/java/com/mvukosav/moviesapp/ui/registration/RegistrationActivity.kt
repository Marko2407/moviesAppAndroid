package com.mvukosav.moviesapp.ui.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.ActivityRegistrationBinding
import com.mvukosav.moviesapp.ui.home.MainActivity
import com.mvukosav.moviesapp.ui.login.LoginActivity
import com.mvukosav.moviesapp.ui.splash.SplashActivity

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        openSplashScreen()
    }

    private fun openMainScreen() {
//        hideProgressBar()
        startActivity(MainActivity.createIntent(this))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun openSplashScreen() {
//        hideProgressBar()
        startActivity(SplashActivity.createIntent(this))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }


    companion object {
        fun createIntent(context: Context) = Intent(context, RegistrationActivity::class.java)
    }
}