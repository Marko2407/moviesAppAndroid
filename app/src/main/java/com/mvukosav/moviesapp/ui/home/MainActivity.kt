package com.mvukosav.moviesapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.ActivityMainBinding
import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.presentation.home.MainActivityViewModel
import com.mvukosav.moviesapp.ui.search.SearchActivity
import com.mvukosav.moviesapp.ui.settings.SettingsActivity
import com.mvukosav.moviesapp.ui.watchlist.WatchListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        fetchUser()
        setupBottomNavigationBar()
        createSideNavigationBar()
        setOnClickListeners()
        drawerListener()
    }

    private fun fetchUser() {
        viewModel.fetchUserData()
        viewModel.userLiveData.observe(this) { user ->
            if (user != null) {
                binding.userProfile.text = user.userInitials
                setUserSideDrawer(user)
            } else {
                logout()
            }
        }
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
                    setCurrentFragment(WatchListFragment())
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
//            startActivity(ProfileActivity.createIntent(this))
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        binding.imgSearchBtn.setOnClickListener {
            startActivity(SearchActivity.createIntent(this))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        binding.navDrawer.logout.setOnClickListener {
            logout()
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

    private fun setUserSideDrawer(user: User) {
        val headerView = binding.navView.getHeaderView(0)
        val navUserName: TextView = headerView.findViewById(R.id.txt_user_name)
        val navUserEmail: TextView = headerView.findViewById(R.id.txt_user_email)
        val navUserInitials: TextView = headerView.findViewById(R.id.user_initials)
        navUserName.text = user.userFullName
        navUserEmail.text = user.email
        navUserInitials.text = user.userInitials
    }

    fun logout() {
        viewModel.signOutUser()
    }

    private fun createSideNavigationBar() {
        val sideNavigationBar = binding.navView
        sideNavigationBar.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    startActivity(SettingsActivity.createIntent(this))
                    drawerLayout.close()
                    return@setNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun observeLogout() {
//        viewModel.logoutResponseLiveData.observe(this) {
//            if (it != null) {
//                startActivity(SplashActivity.createIntent(this))
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//                finish()
//            } else {
//                toastMsg(this, getString(R.string.something_went_wrong_please_try_again))
//            }
//        }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
        private const val WATCH_LIST_FRAGMENT = 2
        private const val HOME_FRAGMENT = 1
    }
}