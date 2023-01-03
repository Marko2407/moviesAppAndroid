package com.mvukosav.moviesapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mvukosav.moviesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgSearchBtn.setOnClickListener{
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
        }

        binding.userProfile.setOnClickListener{
            Toast.makeText(this, "user profile clicked", Toast.LENGTH_SHORT).show()
        }
    }
    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}