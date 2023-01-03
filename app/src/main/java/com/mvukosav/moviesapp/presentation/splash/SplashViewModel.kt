package com.mvukosav.moviesapp.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvukosav.moviesapp.domain.interactors.splash.LoggedInUser
import com.mvukosav.moviesapp.domain.interactors.splash.LogoutUser
import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.network.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loggedInUser: LoggedInUser,
    private val logoutUser: LogoutUser,
) : ViewModel() {

    private val userLiveData = MutableLiveData<User?>()
    val fetchUserLiveData: LiveData<User?> = userLiveData

    suspend fun userLogin() {
        withContext(viewModelScope.coroutineContext) {
            //check if user is login else login user
            when (val response = loggedInUser()) {
                is Response.Result -> {
                    userLiveData.postValue(response.result)
                }
                else -> {
                    userLiveData.postValue(null)
                }
            }
        }
    }

    suspend fun logout() {
        withContext(viewModelScope.coroutineContext) {
            //check if user is login else login user
            when (val response = logoutUser()) {
                else -> {}
            }
        }
    }
}
