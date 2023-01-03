package com.mvukosav.moviesapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.ActivityMainBinding
import com.mvukosav.moviesapp.presentation.home.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private val viewModel: MainActivityViewModel by viewModels()
    private var currentFragment: Int = HOME_FRAGMENT
    private var drawerOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerLayout = binding.mainContainer
        drawerLayout.drawerElevation = 16F
        setupBottomNavigationBar()
        setOnClickListeners()
        drawerListener()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationBar = binding.bottomNavigationView
        setCurrentFragment(HomeFragment())
        bottomNavigationBar.menu.getItem(HOME_FRAGMENT).isChecked = true
        bottomNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    binding.title.text = getString(R.string.movies)
                    setCurrentFragment(HomeFragment())
                    it.isChecked = true
                    currentFragment = HOME_FRAGMENT
                    return@setOnItemSelectedListener true
                }
                R.id.watch_list -> {
                    binding.title.text = getString(R.string.watch_list)
                    setCurrentFragment(HomeFragment())
                    it.isChecked = true
                    currentFragment = WATCH_LIST_FRAGMENT
                    return@setOnItemSelectedListener true
                }
                R.id.userProfile -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            binding.bottomNavigationView.menu.getItem(currentFragment).isChecked = true
        } else {
            super.onBackPressed()
        }
    }

    private fun setOnClickListeners() {
        binding.userProfile.setOnClickListener {
            //TODO("Open user profile")
        }
        binding.imgSearchBtn.setOnClickListener {
            //TODO("Open search screen")
        }
    }


    private fun drawerListener() {
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                if (drawerOpen) {
                    binding.bottomNavigationView.menu.getItem(currentFragment).isChecked = true
                    drawerOpen = false
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                drawerOpen = true
            }

            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerClosed(drawerView: View) {
                drawerOpen = false
            }
        })
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
        private const val WATCH_LIST_FRAGMENT = 2
        private const val HOME_FRAGMENT = 1

    }
}