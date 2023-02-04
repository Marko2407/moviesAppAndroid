package com.mvukosav.moviesapp.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.txtToolbarTitle.text = this.getString(R.string.search)
        setOnClickListeners()
    }
    private fun setOnClickListeners() {
        binding.toolbar.btnToolbarStart.setOnClickListener {
            finish()
        }
        binding.btnLanguage.setOnClickListener {
            Toast.makeText(this, "Successfully selected language", Toast.LENGTH_SHORT).show()
        }
    }
    companion object {
        fun createIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }
}
