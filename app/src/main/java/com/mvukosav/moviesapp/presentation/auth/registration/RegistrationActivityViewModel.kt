package com.mvukosav.moviesapp.presentation.auth.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvukosav.moviesapp.domain.interactors.auth.InputValidation
import com.mvukosav.moviesapp.domain.interactors.auth.login.SignInUser
import com.mvukosav.moviesapp.domain.interactors.auth.registration.CreateUser
import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.network.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegistrationActivityViewModel @Inject constructor(
    private val inputValidation: InputValidation,
    private val createNewUser: CreateUser,
    private val signInUser: SignInUser
) : ViewModel() {

    private val errorLiveData = MutableLiveData<String?>()
    val fetchErrorLiveData: LiveData<String?> = errorLiveData

    private val userLiveData = MutableLiveData<User?>()
    val fetchUserLiveData: LiveData<User?> = userLiveData

    private val inputValidationLiveData = MutableLiveData<Boolean>()
    val fetchInputValidationLiveData: LiveData<Boolean> = inputValidationLiveData

    fun createUser(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                when (val response = createNewUser(fullName, email, password)) {
                    is Response.Result -> {
                        loginUser(response.result, email, password)
                    }
                    is Response.ErrorApi -> {
                        userLiveData.postValue(null)
                        errorLiveData.postValue(response.error.name)
                    }
                    is Response.NetworkError -> {
                        userLiveData.postValue(null)
                        errorLiveData.postValue("Check your network")
                    }
                    else -> {
                        userLiveData.postValue(null)
                        errorLiveData.postValue("Unknown error")
                    }
                }
            }
        }
    }

    private fun loginUser(result: User?, email: String, password: String) {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                when (val response = signInUser(email, password)) {
                    is Response.Result -> {
                        userLiveData.postValue(result)
                    }
                    is Response.ErrorApi -> {
                        errorLiveData.postValue(response.error.name)
                    }
                    is Response.NetworkError -> {
                        errorLiveData.postValue("Check your network")
                    }
                    else -> {
                        errorLiveData.postValue("Unknown error")
                    }
                }
            }
        }
    }

    fun validateInputFields(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        inputValidationLiveData.postValue(
            inputValidation(
                fullName,
                email,
                password,
                confirmPassword
            )
        )
    }
}