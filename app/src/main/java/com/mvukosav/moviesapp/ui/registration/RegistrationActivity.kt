package com.mvukosav.moviesapp.ui.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mvukosav.moviesapp.databinding.ActivityRegistrationBinding
import com.mvukosav.moviesapp.ui.login.LoginActivity

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, RegistrationActivity::class.java)
    }
}