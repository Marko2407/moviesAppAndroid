package com.mvukosav.moviesapp.presentation.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvukosav.moviesapp.domain.interactors.auth.PasswordValidation
import com.mvukosav.moviesapp.domain.interactors.auth.login.SignInUser
import com.mvukosav.moviesapp.domain.interactors.splash.LoggedInUser
import com.mvukosav.moviesapp.domain.models.AuthData
import com.mvukosav.moviesapp.network.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(
    private val signInUser: SignInUser,
    private val loggedInUser: LoggedInUser,
    private val passwordValidation: PasswordValidation
) : ViewModel() {

    private val errorLiveData = MutableLiveData<String?>()
    val fetchErrorLiveData: LiveData<String?> = errorLiveData

    private val userLiveData = MutableLiveData<AuthData?>()
    val fetchUserLiveData: LiveData<AuthData?> = userLiveData

    private val inputValidationLiveData = MutableLiveData<Boolean>()
    val fetchInputValidationLiveData: LiveData<Boolean> = inputValidationLiveData


    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                when (val response = signInUser(email, password)) {
                    is Response.Result -> {
                        fetchUserInfo(response.result)
                    }
                    is Response.ErrorApi -> {
                        userLiveData.postValue(null)
                        errorLiveData.postValue(response.error.name)
                    }
                    else -> {
                        userLiveData.postValue(null)
                    }
                }
            }
        }
    }

    private suspend fun fetchUserInfo(result: AuthData?) {
        withContext(viewModelScope.coroutineContext) {
            when (loggedInUser()) {
                is Response.Result -> {
                    userLiveData.postValue(result)
                }
                else -> {
                    userLiveData.postValue(null)
                    errorLiveData.postValue("Unknown erroe")
                }
            }
        }
    }

    fun validateInput(password: String, email: String) {
        var error = false
        if (!passwordValidation(password)) {
            error = true
        }
        if (!email.contains("@")) {
            error = true
        }


        if (!error) {
            inputValidationLiveData.postValue(true)
        } else {
            inputValidationLiveData.postValue(false)
        }
    }
}